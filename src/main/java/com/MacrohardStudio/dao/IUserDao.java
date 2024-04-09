package com.MacrohardStudio.dao;

import com.MacrohardStudio.model.rootTable.User;
import com.MacrohardStudio.model.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface IUserDao {

    public UserDto login(User user);
    public void register(User user);
    public void modify(User user);
    public User search(User user);
    public User select(String user_id);

}
