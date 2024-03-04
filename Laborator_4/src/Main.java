import Domain.Friendship;
import Domain.User;
import Domain.Validators.FriendshipValidator;
import Domain.Validators.UserValidator;
import Repository.InMemoryRepository.InMemoryRepository;
import Service.Service;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        UserValidator vu = new UserValidator();
        FriendshipValidator vp = new FriendshipValidator();

        InMemoryRepository<UUID, User> repo_utilizator = new InMemoryRepository<>(vu);
        InMemoryRepository<UUID, Friendship> repo_prietenie = new InMemoryRepository<>(vp);

        Service service = new Service(repo_utilizator, repo_prietenie);
        Console console = new Console(service);
        console.run();

//        Tests tests = new Tests(service, repo_utilizator, repo_prietenie);
//        tests.runTests();
    }
}