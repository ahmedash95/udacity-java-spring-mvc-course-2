package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CredentialsMapper {
    @Select("SELECT * FROM credentials WHERE userid=#{userId}")
    @Results({
            @Result(property = "userId", column = "userid")
    })
    List<Credential> findAllByUserId(Long userId);

    @Select("SELECT * FROM credentials WHERE credentialid=#{id}")
    @Results({
            @Result(property = "userId", column = "userid")
    })
    Optional<Credential> findById(Long id);

    @Insert("INSERT INTO credentials (url, username, `key`, password, userid) VALUES (#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    @Results({
            @Result(property = "userId", column = "userid")
    })
    void save(Credential credential);

    @Update("UPDATE credentials SET `url` = #{credential.url}, username = #{credential.username}, `key` = #{credential.key}, password = #{credential.password} WHERE credentialid = #{id}")
    void update(Credential credential, Long id);

    @Delete("DELETE from credentials WHERE credentialid = #{id}")
    void delete(Long id);
}
