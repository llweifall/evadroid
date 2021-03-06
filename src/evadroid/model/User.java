package evadroid.model;

import java.sql.ResultSet;

public class User {
	private UserProfile userProfile;

	public User(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public static User getById(int id) throws Exception{
		UserProfile up = null;

		DBQ dbq = new DBQ("SELECT * FROM user WHERE id=?");
		dbq.set(id);
		ResultSet rs = null;
		try {
			rs = dbq.query();
		}
		catch (Exception e) {
			dbq.close();
			throw(e);
		}
		
		if (rs.next()) {
			up = new UserProfile(rs.getInt("id"), rs.getInt("type"),
					rs.getString("email"), rs.getString("name"),
					rs.getString("password"), rs.getInt("credit"));
		} else {
			dbq.close();
			return null;
		}

		dbq.close();
		return new User(up);
	}
	
	public static User register(int type, String email, String name,
			String password) throws Exception {
		int id = 0;
		DBQ dbq = new DBQ(
				"INSERT INTO user (type, email, name, password, credit) VALUES (?, ?, ?, ?, ?)");
		dbq.set(type);
		dbq.set(email);
		dbq.set(name);
		dbq.set(password);
		dbq.set(0);

		try {
			if(dbq.excute() != 1)
			{
				dbq.close();
				return null;
			}
		} catch (Exception e) {
			dbq.close();
			//return null;
			throw(e);
		}
		id = dbq.getGK().get(0);
		dbq.close();

		UserProfile up = new UserProfile(id, type, email, name, password, 0);
		return new User(up);
	}

	public static User login(String username, String password) throws Exception {
		UserProfile up = null;

		DBQ dbq = new DBQ("SELECT * FROM user WHERE name=? AND password=?");
		dbq.set(username);
		dbq.set(password);
		ResultSet rs = null;
		try {
			rs = dbq.query();
		}
		catch (Exception e) {
			dbq.close();
			throw(e);
		}
		
		if (rs.next()) {
			up = new UserProfile(rs.getInt("id"), rs.getInt("type"),
					rs.getString("email"), rs.getString("name"),
					rs.getString("password"), rs.getInt("credit"));
		} else {
			dbq.close();
			return null;
		}

		dbq.close();
		return new User(up);
	}

	public boolean update() throws Exception {
			DBQ dbq = new DBQ(
					"UPDATE user SET email=?, password=?, name=?, type=?, credit=? WHERE id=?");
			dbq.set(userProfile.getEmail());
			dbq.set(userProfile.getPassword());
			dbq.set(userProfile.getName());
			dbq.set(userProfile.getType());
			dbq.set(userProfile.getCredit());
			dbq.set(userProfile.getId());
			if (dbq.excute() != 1) {
				dbq.close();
				return false;
			}
			dbq.close();
			return true;
	}
	
	public boolean getCredit(int credit) throws Exception {
		userProfile.setCredit(userProfile.getCredit() + credit);
		return update();
	}
}