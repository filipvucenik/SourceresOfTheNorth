package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.DataInitializer;
import com.progi.ostecenja.server.repo.Report;
import org.apache.catalina.session.StandardSession;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ReportControllerTest {
    private static Report saved1;
    private static Report saved2;
    @Autowired
    ReportController reportController;

    @BeforeEach
    void setup()
    {
        saved1 = reportController.createReport(reportInit(), new StandardSession(null), mockImagesInit());
        saved2 = reportController.createReport(reportInit(), new StandardSession(null), mockImagesInit());
    }

    @AfterEach
    void clear()
    {
        reportController.deleteReport(saved1.getReportID());
        reportController.deleteReport(saved2.getReportID());
    }

    private static List<MultipartFile> mockImagesInit(){
        MultipartFile imageMulti;
        try(InputStream is = DataInitializer.class.getClassLoader().getResourceAsStream("mock.png")){
            assert is != null;
            imageMulti = new MockMultipartFile(
                    "data",
                    "mock.png",
                    "image/png",
                    is.readAllBytes()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<MultipartFile> images = new ArrayList<>();
        images.add(imageMulti);
        return images;
    }
    private static Report reportInit(){
        return new Report(null, "problem",45.8000646, 15.978519,"nastao je problem", null,null,null, 1L);
    }

    @Test
    public void updateStatusTest(){
        assertEquals(reportController.getStatus(saved1.getReportID()).getKey().getStatus(), "neobraden");
        reportController.changeStatus(saved1.getReportID(), "uProcesu");
        assertEquals(reportController.getStatus(saved1.getReportID()).getKey().getStatus(), "uProcesu");
    }

    @Test
    public void updateStatusSameUpdate(){
        reportController.changeStatus(saved1.getReportID(), "uProcesu");
        assertThrows(IllegalArgumentException.class,()->{ reportController.changeStatus(saved1.getReportID(), "uProcesu");});
    }

    @Test
    public void updateStatusNonExistingReport(){
        assertThrows(IllegalArgumentException.class,()->{reportController.changeStatus(-1L, "uProcesu");});

    }
    @Test
    public void updateStatusNullReportID(){
        assertThrows(IllegalArgumentException.class,()->{reportController.changeStatus(null, "uProcesu");});

    }

    @Test
    public void groupReportsTest(){
        assertNull(reportController.getReport(saved2.getReportID()).getReport().getGroup());
        reportController.groupReports(saved1, List.of(new Report[]{saved2}));
        assertEquals(reportController.getReport(saved2.getReportID()).getReport().getGroup().getReportID(),saved1.getReportID());
    }

    @Test
    public void groupReportsLeaderDoesNotExists(){
        assertThrows(IllegalArgumentException.class, ()->{ reportController.groupReports(new Report(), List.of(new Report[]{saved2}));});
    }

    @Test
    public void groupReportsLeaderNullTest(){
        assertThrows(IllegalArgumentException.class, ()->{ reportController.groupReports(null, List.of(new Report[]{saved2}));});
    }

    @Test
    public void groupReportsGroupMembersNullTest(){
        assertThrows(IllegalArgumentException.class, ()->{ reportController.groupReports(saved1, null);});
    }

    @Test
    public void groupReportsGroupMembersArrayEmptyTest(){
        assertThrows(IllegalArgumentException.class, ()->{ reportController.groupReports(saved1, List.of(new Report[]{}));});
    }

    @Test
    public void updateGroupStatusTest(){
        assertEquals(reportController.getStatus(saved1.getReportID()).getKey().getStatus(),"neobraden");
        assertEquals(reportController.getStatus(saved2.getReportID()).getKey().getStatus(),"neobraden");
        reportController.groupReports(saved1, List.of(new Report[]{saved2}));
        reportController.changeStatus(saved1.getReportID(), "uProcesu");
        assertEquals(reportController.getStatus(saved1.getReportID()).getKey().getStatus(),"uProcesu");
        assertEquals(reportController.getStatus(saved2.getReportID()).getKey().getStatus(),"uProcesu");

    }

}