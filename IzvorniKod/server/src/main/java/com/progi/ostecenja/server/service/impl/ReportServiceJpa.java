package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.*;
import com.progi.ostecenja.server.dto.ReportByStatusDTO;
import com.progi.ostecenja.server.dto.ReportFeedbackJoin;
import com.progi.ostecenja.server.dto.ReportFilterDto;
import com.progi.ostecenja.server.dto.StatisticDTO;
import com.progi.ostecenja.server.repo.*;
import com.progi.ostecenja.server.service.EntityMissingException;
import com.progi.ostecenja.server.service.FeedbackService;
import com.progi.ostecenja.server.service.ImageService;
import com.progi.ostecenja.server.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ReportServiceJpa implements ReportService {
    @Autowired
    ReportRepository reportRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    FeedbackService feedbackService;
    @Autowired
    ImageService imageService;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    FeedbackRepository feedbackRepository;

    @Override
    public List<Report> listAll(){
        return reportRepo.findAll();
    }

    @Override
    public List<Report> listAllforUsers(long userID) {
        return reportRepo.findAll().stream().filter(r -> r.getUserID().equals(userID)).toList();
    }

    @Override
    public List<Report> listAllforOffice(long cityOfficeID) {
        final List<Long> categories = categoryRepo.findAll().stream()
                .filter(c -> c.getCityOfficeID().equals(cityOfficeID))
                .mapToLong(Category::getCategoryID)
                .boxed().toList();
        return reportRepo.findAll().stream().filter(r -> categories.contains(r.getCategoryID())).toList();
    }

    @Override
    public Report createReport(Report report) {
        return reportRepo.save(report);
    }

    // TODO provjeriti tocno kak se stanje zove
    @Override
    public List<Report> listAllUnhandled() {

        List<Report> ret = reportRepo.findAll().stream().filter(r -> {
            Long id = r.getReportID();
            return !feedbackService.existsFeedback(id, "obraden");
        }).toList();

        return ret;
    }

    @Override
    public Optional<Report> findByUserId(Long reportID) {
        return reportRepo.findById(reportID);
    }

    @Override
    public List<String> getHeadlines() {
        return reportRepo.findAll().stream().map(Report::getReportHeadline).toList();
    }

    @Override
    public Report getReport(Long reportID) {
        return findByUserId(reportID).orElseThrow(
                () -> new EntityMissingException(Report.class, reportID)
        );
    }

    @Override
    public List<Report> getReportsByFilter(ReportFilterDto reportFilterDto) {
        List<ReportFeedbackJoin>   reportFeedbackJoin = reportRepo.findByReportAttributes();

        System.out.println(reportFilterDto.toPrint());
        List<Predicate<ReportFeedbackJoin>> conditionsList = new LinkedList<>();


        if(reportFilterDto.getRadius()!=null && reportFilterDto.getLat()!=null && reportFilterDto.getLng()!=null){
            conditionsList.add( r-> {
                double radius = reportFilterDto.getRadius();
                double latCenter = reportFilterDto.getLat();
                double lngCenter = reportFilterDto.getLng();


                double latDiff = Math.toRadians(r.getReport().getLat() - latCenter);
                double lngDiff = Math.toRadians(r.getReport().getLng() - lngCenter);

                double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                        Math.cos(Math.toRadians(latCenter)) * Math.cos(Math.toRadians(r.getReport().getLat())) *
                                Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);

                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double distance = 6371 * c;

                return distance <= radius;
            });
        }

        if(!reportFilterDto.getStatus().isEmpty()){
            conditionsList.add(r -> r.getFeedback().getKey().getStatus().equals(reportFilterDto.getStatus()));
        }
        if(reportFilterDto.getCategoryId()!=null){
            conditionsList.add(r-> Objects.equals(r.getReport().getCategoryID(), reportFilterDto.getCategoryId()));
        }

        if(reportFilterDto.getStartDate()!=null && reportFilterDto.getEndDate()!=null){
            conditionsList.add(r-> r.getReport().getReportTS().after(reportFilterDto.getStartDate()) && r.getReport().getReportTS().before(reportFilterDto.getEndDate()));
        } else if(reportFilterDto.getStartDate()!=null){
            conditionsList.add(r->r.getReport().getReportTS().after(reportFilterDto.getStartDate()));
        } else if(reportFilterDto.getEndDate()!=null){
            conditionsList.add(r->r.getReport().getReportTS().before(reportFilterDto.getEndDate()));
        }

        System.out.println(conditionsList.size());

        Predicate<ReportFeedbackJoin> combinedPredicate = conditionsList.stream()
                .reduce(Predicate::and)
                .orElse(x -> true);

        return reportFeedbackJoin.stream()
                .filter(combinedPredicate)
                .map(ReportFeedbackJoin::getReport)
                .collect(Collectors.toList());
    }
    @Override
    public ReportByStatusDTO getReportsByUserId(Long userID){
       List<ReportFeedbackJoin> reports =reportRepo.findByUserIDAndJoinWithFeedback(userID);
       int waitingCount=0;
       int inProgressCount=0;
       int solvedCount=0;
       List<ReportFeedbackJoin> reportsToSend = new LinkedList<>();

      for(ReportFeedbackJoin r: reports) {
          reportsToSend.add(r);
          if (r.getFeedback().getKey().getStatus().equals("neobrađen")) {
              waitingCount++;
          } else if (r.getFeedback().getKey().getStatus().equals("uProcesu")) {
              inProgressCount++;
          } else if (r.getFeedback().getKey().getStatus().equals("obrađen")) {
              solvedCount++;
          }
      }

       return new ReportByStatusDTO(reportsToSend, waitingCount, inProgressCount,solvedCount)  ;
    }
    @Transactional
    @Override
    public void groupReports(Report groupLeader, List<Long> members) {
        if(groupLeader == null)
            throw new IllegalArgumentException("group leader is null");

        if(members == null || members.isEmpty())
            throw new IllegalArgumentException("members are empty or null");
        try{
            if(!reportRepo.existsById(groupLeader.getReportID())){
                throw new IllegalArgumentException("group leader does not exists");
            }
        }catch (org.springframework.dao.InvalidDataAccessApiUsageException e){
            throw new IllegalArgumentException("group leader does not exists");
        }

        for(Long memberid: members){
            Report member = reportRepo.getReferenceById(memberid);
            member.setGroup(groupLeader);
            reportRepo.save(member);
        }
    }

    @Override
    @Transactional
    public void delete(long reportId) {
        if(!reportRepo.existsById(reportId))
            return;
        Report toBeDeleted = reportRepo.getReferenceById(reportId);
        List<Report> members = new ArrayList<>(reportRepo.findAll().stream().filter(r -> r.getGroup() != null).filter(r -> r.getReportID().equals(reportId)).toList());
        if(!members.isEmpty()){
            reportRepo.deleteAll(members);
            List<Long> delReportIds = members.stream().map(Report::getReportID).toList();
            List<Image> delImages = new ArrayList<>();
            for(Long id: delReportIds){
                Optional<Image> del =imageRepository.findImageByReportID(id);
                del.ifPresent(delImages::add);
            }
            imageRepository.deleteAll(delImages);
        }
        reportRepo.delete(toBeDeleted);
        Optional<Image> imageDel = imageRepository.findImageByReportID(toBeDeleted.getReportID());
        imageDel.ifPresent(image -> imageRepository.delete(image));
        /*
        String[] statusi = new String[]{"neobraden","uProcesu","obrađen"};

        List<Feedback> delFeedbacks;
        List<FeedbackID> delFeedbackIds = new ArrayList<>();
        for(Long id: delReportIds){
            for(String status: statusi){
                FeedbackID fid = new FeedbackID(id, status);
                if(feedbackRepository.existsById(fid)){
                    delFeedbackIds.add(fid);
                }
            }
        }

        delFeedbacks = feedbackRepository.findAllById(delFeedbackIds);
        feedbackRepository.deleteAll(delFeedbacks);

         */


    }
    public StatisticDTO getReportStatistic(ReportFilterDto reportFilterDto){
        StatisticDTO result = new StatisticDTO();
        int reportCount;

        int reportWaitingCount=0;
        double reportWaitingShare;

        int reportInProgressCount= 0;
        double reportInProgressShare;

        int reportSolvedCount = 0;
        double reportSolvedShare;

        double avgReportsByDay;
        Duration avgTimeWaiting;
        Duration avgTimeInProgress;

        List<Report> reports = getReportsByFilter(reportFilterDto);

        Map<LocalDate, Long> reportsPerDay = reports.stream()
                .collect(Collectors.groupingBy(
                        report -> report.getReportTS().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.counting()
                ));

        avgReportsByDay = reportsPerDay.values().stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);

        reportCount = reports.size();

        if(reportCount == 0) return result;

        Duration waitingSum = Duration.ZERO;
        int waitingDenominator = 0;

        Duration inProgresSum = Duration.ZERO;
        int inProgressDenominator = 0;

        List<Feedback> allFeedbacks = feedbackService.getAll();

        allFeedbacks.removeIf(feedback -> !reports.stream()
                .map(Report::getReportID)
                .distinct()
                .toList()
                .contains(feedback.getKey().getGroupID()));
        Collections.sort(allFeedbacks, Comparator.comparing(f->f.getKey().getGroupID()));

        List<String> feedbackStatusOrder = List.of("neobrađen", "uProcesu", "obrađen");
        Comparator<Feedback> statusComparator = Comparator.comparingInt(
                feedback -> feedbackStatusOrder.indexOf(feedback.getKey().getStatus()));

        Map<Long, List<Feedback>> feedbackMap = allFeedbacks.stream()
                .collect(Collectors.groupingBy(
                        feedback -> feedback.getKey().getGroupID(),
                        Collectors.mapping(feedback -> feedback, Collectors.toList())));

        //Print ne brisati
      /*feedbackMap.forEach((key, value) -> {
               System.out.println("GroupID: " + key + ", Feedbacks: " );
               value.forEach( f ->System.out.println("          "+f.getChangeTS() + " " + f.getKey().getGroupID() + " " + f.getKey().getStatus() ));
           System.out.println();

       });*/

        for(List<Feedback> m: feedbackMap.values()){
            if(m.size()== 1){
                reportWaitingCount++;

            } else {
                if(m.size()==2){
                    reportInProgressCount++;

                    waitingSum = waitingSum.plus(Duration.between( m.get(0).getChangeTS().toInstant(),m.get(1).getChangeTS().toInstant()));
                    waitingDenominator++;
                } else {
                    if(m.size()==3){
                        waitingSum = waitingSum.plus(Duration.between( m.get(0).getChangeTS().toInstant(),m.get(1).getChangeTS().toInstant()));
                        waitingDenominator++;

                        inProgresSum = inProgresSum.plus(Duration.between(m.get(1).getChangeTS().toInstant(), m.get(2).getChangeTS().toInstant()));
                        inProgressDenominator++;

                        reportSolvedCount++;
                    }
                }
            }
        }


        avgTimeWaiting = (waitingDenominator>0) ? Duration.ofMillis(waitingSum.toMillis()/ waitingDenominator): null;
        avgTimeInProgress =  (inProgressDenominator>0) ? Duration.ofMillis(inProgresSum.toMillis()/inProgressDenominator): null;


        reportWaitingShare = (double) reportWaitingCount / reportCount;
        reportInProgressShare = (double) reportInProgressCount / reportCount;
        reportSolvedShare = (double) reportSolvedCount / reportCount;

        result.setReportCount(reportCount);
        result.setAvgReportsByDay(avgReportsByDay);

        if(avgTimeWaiting !=null)
            result.setAvgTimeWaiting(avgTimeWaiting.toDaysPart() + ","+ avgTimeWaiting.toHoursPart() + "," + avgTimeWaiting.toMinutesPart());

        if(avgTimeInProgress !=null)
            result.setAvgTimeInProgress(avgTimeInProgress.toDaysPart() + ","+ avgTimeInProgress.toHoursPart() + "," + avgTimeInProgress.toMinutesPart());

        result.setReportInProgressShare(reportInProgressShare);
        result.setReportInProgressCount(reportInProgressCount);

        result.setReportSolvedCount(reportSolvedCount);
        result.setReportSolvedShare(reportSolvedShare);

        result.setReportWaitingShare(reportWaitingShare);
        result.setReportWaitingCount(reportWaitingCount);
        return  result;
    }
    public List<Report> getReportsByStatus(String status, Long officeID){
        List<Report> reports = reportRepo.findByOfficeIdAndStatus(officeID, status);
        return reports;
    }
}
