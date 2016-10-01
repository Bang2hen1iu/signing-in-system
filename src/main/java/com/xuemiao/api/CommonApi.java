package com.xuemiao.api;

import com.xuemiao.model.pdm.StudentEntity;
import com.xuemiao.model.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by dzj on 10/1/2016.
 */
@Path("/common_api")
public class CommonApi {
    @Autowired
    StudentRepository studentRepository;

    @GET
    @Path("/students")
    public Response getStudents(){
        List<StudentEntity> studentEntities = studentRepository.findAll();
        return Response.ok().entity(studentEntities).build();
    }


}
