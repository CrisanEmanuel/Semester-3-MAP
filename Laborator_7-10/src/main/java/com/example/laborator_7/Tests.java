package com.example.laborator_7;

import com.example.laborator_7.Domain.Friendship;
import com.example.laborator_7.Domain.Message;
import com.example.laborator_7.Domain.User;
import com.example.laborator_7.Repository.Paging.Pageable;
import com.example.laborator_7.Repository.Paging.PageableImplementation;
import com.example.laborator_7.Repository.Paging.PagingRepository;
import com.example.laborator_7.Repository.Repository;

import java.util.UUID;

public class Tests {
//    private final Repository<UUID, User> repo_user;
//    private final Repository<UUID, Friendship> repo_friendship;
    private final Repository<UUID, User> userDBrepo;
    private final Repository<UUID, Friendship> friendshipDBRepo;
    private final Repository<UUID, Message> messageDBRepository;

    private final PagingRepository<UUID, User> userDBpaging;


    public Tests(Repository<UUID, User> userDBrepo, Repository<UUID, Friendship> friendshipDBRepo, Repository<UUID, Message> messageDBRepo, PagingRepository<UUID, User> userDBpaging) {
        this.userDBrepo = userDBrepo;
        this.friendshipDBRepo = friendshipDBRepo;
        this.messageDBRepository = messageDBRepo;
        this.userDBpaging = userDBpaging;
    }

    public void runTests() {

        //userDBrepo.findAll().forEach(System.out::println);
        Pageable pageable = new PageableImplementation(2,3);
        var content = userDBpaging.findAll(pageable).getContent().toList();
        if(!content.isEmpty()) {
            content.forEach(System.out::println);
        }else {
            System.out.println("pag nu exista");
        }

        System.out.println("All good!");
    }

}