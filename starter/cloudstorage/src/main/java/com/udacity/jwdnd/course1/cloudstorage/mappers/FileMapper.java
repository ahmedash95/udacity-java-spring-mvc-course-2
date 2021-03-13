package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM files WHERE userId=#{userId}")
    List<File> findAllByUserId(Long userId);

    @Select("SELECT * FROM files")
    List<File> findAll();

    @Select("SELECT * FROM files WHERE fileId=#{id}")
    Optional<File> findById(Long id);

    @Insert("INSERT INTO files (filename, contentType, fileSize, fileData, userId) VALUES (#{filename}, #{contentType}, #{fileSize}, #{fileData}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void save(File file);

    @Delete("DELETE from files WHERE fileId=#{id}")
    void delete(Long id);

    @Select("SELECT * FROM files WHERE filename=#{name} and userid=#{userId}")
    Optional<Object> findByNameAndUser(String name, Long userId);
}
