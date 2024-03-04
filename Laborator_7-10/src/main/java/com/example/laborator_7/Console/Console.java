package com.example.laborator_7.Console;

import com.example.laborator_7.Domain.Friendship;
import com.example.laborator_7.Domain.User;
import com.example.laborator_7.Service.Service;

import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Console {
    Service service;

    public Console(Service s){
        this.service = s;
    }

    public void run(){
        menu();
        while(true) {
            System.out.println(">>>");
            Scanner cmd = new Scanner(System.in);
            int i = cmd.nextInt();
            if (i == 1) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("First name : ");
                String firstName = scanner.nextLine();
                System.out.println("Last name : ");
                String lastName = scanner.nextLine();
                System.out.println("Email: ");
                String email = scanner.nextLine();
                addUser(firstName, lastName, email);
            }
            else if (i == 2) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter the email of the deleted user : ");
                String email = scanner.nextLine();
                deleteUser(email);
            }
            else if (i == 3) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter email for 1st user : ");
                String email1 = scanner.nextLine();
                System.out.println("Enter email for 2nd user : ");
                String email2 = scanner.nextLine();
                addFriend(email1,email2);
            }
            else if (i == 4) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter email for 1st user : ");
                String email1 = scanner.nextLine();
                System.out.println("Enter email for 2nd user : ");
                String email2 = scanner.nextLine();
                acceptFriendRequest(email1,email2);
            }
            else if (i == 5) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter email for 1st user : ");
                String email1 = scanner.nextLine();
                System.out.println("Enter email for 2nd user : ");
                String email2 = scanner.nextLine();
                declineFriendRequest(email1,email2);
            }
            else if (i == 6) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter email for 1st user : ");
                String email1 = scanner.nextLine();
                System.out.println("Enter email for 2nd user : ");
                String email2 = scanner.nextLine();
                deleteFriendship(email1, email2);
            }
            else if (i == 7) {
                getAllUsers();
            }
            else if (i == 8) {
                getAllFriendships();
            }
            else if (i == 9) {
                communitiesNumber();
            }
            else if (i == 10) {
                mostSociableCommunity();
            }
            else if (i == 11) {
                addData();
            }
            else if (i == 12) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Number of the month: ");
                int monthNumber = Integer.parseInt(scanner.nextLine());
                System.out.println("User email: ");
                String userEmail = scanner.nextLine();
                DBshowFriendshipsMadeInACertainMonth(monthNumber, userEmail);
            }
            else if (i == 0){
                break;
            }
        }
    }

    private void addUser(String ln, String fn, String e){
        User u = new User(ln, fn,e);
        try {
            this.service.addUser(u);
        }
        catch(Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    private void deleteUser(String email){
        try{
            this.service.deleteUser(email);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void addFriend(String email1, String email2){
        try {
            this.service.createFriendship(email1, email2);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void acceptFriendRequest(String email1, String email2){
        try {
            this.service.acceptFriendRequest(email1, email2);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void declineFriendRequest(String email1,String email2){
        try{
            this.service.declineFriendRequest(email1, email2);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void deleteFriendship(String email1, String email2){
        try {
            this.service.deleteFriendship(email1, email2);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void getAllUsers(){
        Iterable <User> lista = service.getAllUsers();
        for(User u : lista){
            System.out.println(u);
        }
    }

    private void getAllFriendships(){
        Iterable <Friendship> lista = service.getAllFriendships();
        lista.forEach(System.out::println);
    }

    private void communitiesNumber(){
        System.out.println("The number of communities is: " + service.communitiesNumber());
    }

    private void mostSociableCommunity(){
        System.out.println("Biggest community is : " + service.mostSociableCommunity());
    }

    private void addData(){
        service.addData();
    }

    private void DBshowFriendshipsMadeInACertainMonth(int monthNumber, String userEmail) {
        try {
            Set<Friendship> friendships = service.DBShowFriendshipsMadeInACertainDate(monthNumber, userEmail);
            friendships.forEach(friendship -> {
                if (!Objects.equals(friendship.getUser1().getEmail(), userEmail)) {
                    System.out.println(friendship.getUser1().getFirstName() + " | " + friendship.getUser2().getLastName() + " | " + friendship.getDate());
                }
                if (!Objects.equals(friendship.getUser2().getEmail(), userEmail)) {
                    System.out.println(friendship.getUser1().getFirstName() + " | " + friendship.getUser2().getLastName() + " | " + friendship.getDate());
                }
            });
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void menu(){
        System.out.println("________ MENU ________");
        System.out.println("1. Add user");
        System.out.println("2. Delete user");
        System.out.println("3. Add friend");
        System.out.println("4. Accept friend request");
        System.out.println("5. Decline friend request");
        System.out.println("6. Delete friendship");
        System.out.println("7. Show all users");
        System.out.println("8. Show friendship relations");
        System.out.println("9. Number of communities");
        System.out.println("10. Biggest community");
        System.out.println("11. Add data");
        System.out.println("12. Show friendships user with date");
        System.out.println("0. Exit");
    }
}
