public interface UserService {
  UserResponse register(RegisterRequest request);

  JwtAuthentication login(LoginRequest request);

  UserResponse getCurrentUser();
}