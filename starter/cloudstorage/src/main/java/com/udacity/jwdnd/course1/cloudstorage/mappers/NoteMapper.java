package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.*;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM notes WHERE userId=#{userId}")
    @Results({
            @Result(property = "id", column = "noteid"),
            @Result(property = "title", column = "notetitle"),
            @Result(property = "description", column = "notedescription")
    })
    List<Note> findAllByUserId(Long userId);

    @Select("SELECT * FROM notes WHERE noteid=#{id}")
    @Results({
            @Result(property = "id", column = "noteid"),
            @Result(property = "title", column = "notetitle"),
            @Result(property = "description", column = "notedescription")
    })
    Optional<Note> find(Long id);

    @Update("UPDATE notes SET notetitle = #{title}, notedescription = #{description} WHERE noteid = #{id}")
    void update(Note note);

    @Delete("DELETE from notes WHERE noteid=#{id}")
    void delete(Long id);

    @Insert("INSERT INTO notes (notetitle, notedescription, userId) VALUES (#{title}, #{description}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Note note);

}
