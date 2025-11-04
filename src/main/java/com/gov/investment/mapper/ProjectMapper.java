package com.gov.investment.mapper;

import com.gov.investment.model.Project;
import org.apache.ibatis.annotations.*;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ProjectMapper {
    
    @Insert("INSERT INTO project (id, project_name, project_code, description, status, create_time, update_time) " +
            "VALUES (#{id}, #{projectName}, #{projectCode}, #{description}, #{status}, #{createTime}, #{updateTime})")
    int insertProject(Project project);
    
    @Select("SELECT * FROM project WHERE id = #{id}")
    Project selectProjectById(BigDecimal id);
    
    @Update("UPDATE project SET project_name = #{projectName}, project_code = #{projectCode}, description = #{description}, " +
            "status = #{status}, update_time = #{updateTime} WHERE id = #{id}")
    int updateProject(Project project);
    
    @Delete("DELETE FROM project WHERE id = #{id}")
    int deleteProjectById(BigDecimal id);
    
    @Select("SELECT * FROM project")
    List<Project> selectAllProjects();
}
