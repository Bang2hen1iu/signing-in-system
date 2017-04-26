package com.xuemiao.api;

import com.xuemiao.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.File;
import java.sql.Date;

/**
 * Created by root on 16-10-19.
 */
@Component
@Path("/statistics")
public class StatisticsApi {
    @Autowired
    StatisticsService statisticsService;
    @Value("${range-statistics-dir}")
    String rangeStatisticsDir;

    //get statistics of this month
    @GET
    @Path("/sum")
    public Response getStatisticsSum() {
        return Response.ok().entity(statisticsService.getStatisticsOfThisMonth()).build();
    }

    //get statistics by date range
    @GET
    @Path("/range_query")
    public Response rangeQueryStatistics(@QueryParam("startDate") Date startDate, @QueryParam("endDate") Date endDate) {
        return Response.ok().entity(statisticsService.getRangeStatistics(startDate, endDate)).build();
    }

    @GET
    @Path("/download/{downloadCode}")
    @Produces("application/vnd.ms-excel")
    public Response downloadStatistics(@PathParam("downloadCode") String downloadCode){
        File file = new File(rangeStatisticsDir + downloadCode + ".xls");
        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=new-excel-file.xls");
        return response.build();
    }
}
