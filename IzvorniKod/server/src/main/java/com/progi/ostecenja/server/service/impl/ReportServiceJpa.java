package com.progi.ostecenja.server.service.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.progi.ostecenja.server.dao.*;
import com.progi.ostecenja.server.dto.ReportByStatusDTO;
import com.progi.ostecenja.server.dto.ReportFeedbackJoin;
import com.progi.ostecenja.server.dto.ReportFilterDto;
import com.progi.ostecenja.server.dto.StatisticDTO;
import com.progi.ostecenja.server.repo.*;
import com.progi.ostecenja.server.service.EntityMissingException;
import com.progi.ostecenja.server.service.FeedbackService;
import com.progi.ostecenja.server.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceJpa implements ReportService {
    @Autowired
    ReportRepository reportRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    FeedbackService feedbackService;

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
        if(reportFilterDto.getRadius()== null){
            reportFilterDto.setRadius(100.);
            reportFilterDto.setLng(200.);
            reportFilterDto.setLat(200.);

        }
        if(reportFilterDto.getStatus().equals(""))
            reportFilterDto.setStatus(null);

        return
                reportRepo.findByReportAttributes(reportFilterDto.getCategoryId(), reportFilterDto.getStatus(),
                        reportFilterDto.getRadius(),reportFilterDto.getLat(),reportFilterDto.getLng(), reportFilterDto.getStartDate(),
                        reportFilterDto.getEndDate()).stream().distinct().toList();

    }
    @Override
    public List<ReportByStatusDTO> getReportsByUserId(Long userID){
       List<ReportFeedbackJoin> reports =reportRepo.findByUserIDAndJoinWithFeedback(userID);
       int waitingCount=0;
       int inProgressCount=0;
       int solvedCount=0;

      for(ReportFeedbackJoin r: reports) {
          if (r.getFeedback().getKey().getStatus().equals("neobrađen")) {
              waitingCount++;
          } else if (r.getFeedback().getKey().getStatus().equals("uProcesu")) {
              inProgressCount++;
          } else if (r.getFeedback().getKey().getStatus().equals("obrađen")) {
              solvedCount++;
          }
      }
      List<ReportByStatusDTO> result = new LinkedList<>();

      for(ReportFeedbackJoin r: reports){
          result.add( new ReportByStatusDTO(r.getReport(), waitingCount,inProgressCount,solvedCount));
      }

       return  result;
    }

    @Override
    public void groupReports(Report groupLeader, List<Report> members) {
        for(Report member: members){
            member.setGroup(groupLeader);
            reportRepo.save(member);
        }
    }

    @Override
    public void delete(long reportId) {
        Report toBeDeleted = reportRepo.getReferenceById(reportId);
        reportRepo.delete(toBeDeleted);
        List<Report> members = reportRepo.findAll().stream().filter(r->r.getReportID().equals(reportId)).toList();
        if(!members.isEmpty())
            reportRepo.deleteAll(members);
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
