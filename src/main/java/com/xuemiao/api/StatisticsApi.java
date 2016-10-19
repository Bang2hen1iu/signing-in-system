package com.xuemiao.api;

import com.xuemiao.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.sql.Date;

/**
 * Created by root on 16-10-19.
 */
@Path("/statistics")
public class StatisticsApi {
    @Autowired
    StatisticsService statisticsService;

    //get date list of statistics
    @GET
    @Path("/date")
    public Response getStatisticsDate() {
        return Response.ok().entity(statisticsService.getAllDate()).build();
    }

    //get latest date of statistics
    @GET
    @Path("/latest_date")
    public Response getStatisticsLatestDate() {
        return Response.ok().entity(statisticsService.getLatestDate()).build();
    }

    //get statistics by date
    @GET
    @Path("/{date}")
    public Response getStatisticData(@PathParam("date") Date date) {
        return Response.ok().entity(statisticsService.getStatisticsByDate(date)).build();
    }

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
}
