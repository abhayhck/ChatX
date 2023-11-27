# ChatX
Chatting Application
Project Overview:
1. Authentication:
	Login Activity:
		Allows existing users to log in.
		Upon successful login, redirects to the Main Activity.
	Signup Activity:
		For new users, collects name, email, and password to create an account.
2. Main Activity:
	User List:
		Displays a list of users with an "Add" button.
		Tapping on a user opens a chat interface for that user.
		Users can add new friends by email, and the app checks the validity of the user.
	
 Chat Interface:
		Allows users to send and receive messages.
		Messages are stored in Firebase under the "Messages" node in a unique room for each chat.
		Uses Firebase Realtime Database to provide real-time updates.
	
 Toolbar:
		Displays the app name "ChatX."
		Contains a menu with a "Logout" option.
		Logging out redirects the user to the Login Activity.
	
3. Firebase Database Structure:
	"User" Node:
		Contains subnodes with UID as keys.
		Each subnode includes user details such as UID, name, and email.

	"Messages" Node:
		Contains subnodes for unique message rooms between two users.
		Each room stores the messages exchanged between the two users.

	"UserContacts" Node:
		Contains subnodes of UIDs for all users.
		Each UID subnode includes a list of users they have added as friends.
	
5. Functionality:
	Direct Login:
		Users are not prompted to log in every time they open the app.
		Provides a seamless experience.

	User Interaction:
		Users can add friends using their email.
		Toast messages indicate whether the user was successfully added or not.

	Logout:
		Allows users to log out from the app.
		Provides a secure logout option.
	
7. User Experience:
	Intuitive UI:
		The application provides a clean and user-friendly interface.
		Easy navigation between activities.

	Real-time Updates:
		Utilizes Firebase Realtime Database for instant message updates.
		Users can see messages as they are sent.

	Technologies Used:
		Programming Language: Kotlin
		Database: Firebase Realtime Database
		Development Environment: Android Studio

	**Conclusion:**
		Your chat application, "ChatX," provides a seamless and secure experience for users to connect, chat, and manage their contacts. The integration with Firebase 			ensures real-time updates and efficient data storage, enhancing the overall user experience.
