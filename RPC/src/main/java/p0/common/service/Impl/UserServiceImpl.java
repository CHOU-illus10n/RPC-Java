package p0.common.service.Impl;

import p0.common.pojo.User;
import p0.common.service.UserService;

import java.util.Random;
import java.util.UUID;

/**
 * @author zwy
 * @version 1.0
 * @description: TODO
 * @date 2024/7/2 18:25
 */
public class UserServiceImpl implements UserService {

    @Override
    public User getUserByUserId(Integer id) {
        System.out.println("客户端查询了"+id+"的用户信息");
        Random random = new Random();
        User user = User.builder()
                .userName(UUID.randomUUID().toString())
                .id(id)
                .sex(random.nextBoolean())
                .build();
        return user;
    }
}
