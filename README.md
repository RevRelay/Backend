# Project 3: RevRelay
RevRelay is a social network for anyone with access to internet, to keep up with friends/family, etc. Every day new users join to share their passions and ideas for people to see and connect with others that share the same passion.

## Tech Stack
- RDBMS for persistence
- API built with Java 8 and Spring 5
- UI built with React JS, JavaScript, HTML, and CSS
- Java API will use Hibernate to communicate with a PostGreSQL RDBMS
- Java API will use Maven to resolve dependencies
- Java API will leverage the Spring Framework utilizing Spring Data, Spring Boot, Spring ORM, and Spring AOP
- Java API will be RESTful 
- Complete CI/CD pipelines will use AWS (CodePipeline, CodeBuild, Elastic Beanstalk, and S3)
- RDBMS will be deployed to the cloud (AWS RDS)
- Java API will be deployed to the cloud (AWS EC2)
- UI application will be deployed to the cloud (AWS S3)
- Java API will have >=80% test coverage for service layer leveraging JUnit and Movkito
- Java API will leverage Spring's MockMVC for integration/e2e tests of controller endpoints
- UI will leverage Jest and Enzyme for testing
- Java API will be secured using Spring Security
- Java API will be adequately documented (Java Docs and web endpoint documentation [Swagger/OpenAPI])
- UI will leverage Axios for managing HTTP requests/responses
- NodeJS with Socket.io will be leveraged for facilitating chats

## User Story Goals
- [x] **Register Account**
  - As a User, I should be able to register a new account.
- [x] **Login**
  - As a User, I should be able to log in to the application.
- [ ] **User Profile**
  - As a User, I should be able to create and maintain a profile page that is visible to other users.
- [ ] **Create/Join a Group/Team**
  - As a User, I should be able to create or join a group/team where there can be collaborative posts only for the group/team to view.
- [x] **Chatroom**
  - As a User, I should be able to join a live chatroom with others and only see messages when I am in the room.
    - Only see the messages sent after you join a chat
- [ ] **Search for Other People**
  - As a User, I should be able to search for other people and go to their profiles.
- [x] **Create a Post**
  - As a User, I should be able to create a post with text and images.
- [ ] **Like a Post**
  - As a User, I should be able to like another user's post.
- [x] **Comment**
  - As a User, I should be able to comment on posts or reply to comments on posts.
- [ ] **Host/Join Events**
  - As a User, I should be able to host or join events on the social media platform. 
- [ ] **Follows/Friends**
  - As a User, I should be able to follow another user to see their posts on my feed. My feed should only displays posts from user that I follow.
- [ ] **Reset Password**
  - As a User, I should be able to reset my password.
- [ ] **Upload Profile Picture**
  - As a User, I should be able to upload an image as a profile picture.
- [ ] **Post Feed**
  - As a User, I should have a post feed that displays other user's posts.
- [ ] **Bookmark Posts**
  - As a User, I should be able to bookmark or save specific posts in order to reference them later.
- [ ] **Notifications**
  - As a User, I should receive notifications when another user interacts with my post or follows me.
- [ ] **Youtube Links**
  - As a User, I should be able to add YouTube links to my posts.
- [ ] **Auto-remove Posts Option**
  - As a User, I should be able to select an option for my account that will remove my posts automatically after a given time frame.
- [ ] **Profanity Filter**
  - Integrate a profanity filter api to ensure that no posts can be submitted with profanity in them to ensure a professional social media environment.
- [ ] **"See First" Option for Other Users**
  - As a User, I should be able to choose other users that I want to see first in my feed. Their posts should show before others.
- [x] **Dark Mode**
  - As a User, I should be able to change the color scheme from the normal mode to a dark mode option.
- [x] **Session Management**
  - As a User, my session should be maintained until I log out.

## Front End
https://github.com/RevRelay/RevRelay.git
