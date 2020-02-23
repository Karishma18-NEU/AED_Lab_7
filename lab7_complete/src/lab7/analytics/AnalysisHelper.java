/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab7.analytics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toMap;
import lab7.entities.Comment;
import lab7.entities.Post;
import lab7.entities.User;

/**
 *
 * @author harshalneelkamal
 */
public class AnalysisHelper {
    // find user with Most Likes
    //  key: UserId ; Value: sum of likes from all comments
    public void userWithMostLikes() {
        Map<Integer, Integer> userLikesCount = new HashMap<>();
        Map<Integer, User> users = DataStore.getInstance().getUsers();
    
        for (User user : users.values()) {
            for (Comment c : user.getComments()) {
                int likes = 0;
                if (userLikesCount.containsKey(user.getId())) {
                    likes = userLikesCount.get(user.getId());
                }
                likes += c.getLikes();
                userLikesCount.put(user.getId(), likes);
            }
        }
        int max = 0;
        int maxId = 0;
        for (int id : userLikesCount.keySet()) {
            if (userLikesCount.get(id) > max) {
                max = userLikesCount.get(id);
                maxId = id;
            }
        }
        System.out.println("User with most likes: " + max + "\n" 
            + users.get(maxId));
    }
    
    // find 5 comments which have the most likes
    public void getFiveMostLikedComment() {
        Map<Integer, Comment> comments = DataStore.getInstance().getComments();
        List<Comment> commentList = new ArrayList<>(comments.values());
        
        Collections.sort(commentList, new Comparator<Comment>() {
            @Override 
            public int compare(Comment o1, Comment o2) {
                return o2.getLikes() - o1.getLikes();
            }
        });
        
        System.out.println("5 most likes comments: ");
        for (int i = 0; i < commentList.size() && i < 5; i++) {
            System.out.println(commentList.get(i));
        }
    }
    
    // @author Vardhana
    public void getAverageLikesPerComment(){
        Map<Integer,Comment> comments = DataStore.getInstance().getComments();
        List<Comment> commentList = new ArrayList<>(comments.values());
        double total_likes = 0;
        int comment_num = commentList.size();
        
        for(Comment comment : commentList){
            total_likes+=comment.getLikes();
        }
        
        double average_likes = total_likes/comment_num;
        System.out.println("The average number of all comments is " + average_likes);
     }
   
    // @author Vardhana
    public void getPostByMostLikedComments(){
        Map<Integer, Post> postHashMap = DataStore.getInstance().getPosts();
        Map<Integer,Integer> tempPostHashMap = new HashMap<>();
        for(Post p:postHashMap.values()){
            for(Comment c:p.getComments()){
            int likes = 0;
            if(tempPostHashMap.containsKey(p.getPostId())){
                likes = tempPostHashMap.get(p.getPostId());
            }
            likes+=c.getLikes();
            tempPostHashMap.put(p.getPostId(), likes);
            }
        }
        int max = 0;
        int maxId = 0;
        for(int id:tempPostHashMap.keySet()){
            if(tempPostHashMap.get(id)>max){
                max = tempPostHashMap.get(id);
                maxId = id;
            }
        }
         System.out.println("Post with most likes: " + max + "\n"
                + postHashMap.get(maxId)+maxId);
    }
    
    // @author Vardhana
    public void getPostByMostComments(){
         Map<Integer, Post> postHashMap = DataStore.getInstance().getPosts();
        List<Post> postList = new ArrayList<>(postHashMap.values());
        
        
        Collections.sort(postList, new Comparator<Post>() {
            @Override
            public int compare(Post p1, Post p2) {
                return p2.getComments().size()-p1.getComments().size();
            }
        });
        
        System.out.println("Post with most comments: "+postList.get(0).getComments().size()+"\n"
                +postList.get(0));
    }
    
    
     // @author Shalini Chandra
    
    //****************************Top 5 inactive users based on total posts number*******************//
    public void getFiveInactiveUsersByPostNum(){
        
        Map<Integer,Post> post = DataStore.getInstance().getPosts();
        List<Post> postList = new ArrayList<>(post.values());
        Map<Integer,Integer> userPostCount = new HashMap<>();
     
        for(Post siglePost : postList){
            int userId = siglePost.getUserId();
            if(!userPostCount.containsKey(userId)){
                int postNum = 1;
                userPostCount.put(userId, postNum);   
            }else{
                int postNum = userPostCount.get(userId);
                userPostCount.put(userId, ++postNum);
            }
        }
        
        Map<Integer, Integer> sortedUserPostCount = userPostCount.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue())
//                .forEach(System.out::println);
                .collect(toMap(Map.Entry::getKey,
                                Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
        
        System.out.println(sortedUserPostCount);
        List<Integer> userList = new ArrayList<>(sortedUserPostCount.keySet());
        System.out.println("Five most Inactive user by post number: ");
        for(int i=0;i<5;i++){
            System.out.print("#" + (i+1) +" UserId: " + userList.get(i) + " --- ");
            System.out.println(DataStore.getInstance().getUsers().get(userList.get(i)));
        }
        
    }
    // @author Shalini Chandra
    //******************Top 5 inactive users based on total comments they created*********************//
    
    public void getFiveInactiveUsersByCreateCommentsNum(){
        Map<Integer, Comment> commentsMap = DataStore.getInstance().getComments();
        Map<Integer, Integer> userCommentCount = new HashMap<>();
        commentsMap.entrySet()
                .stream()
                .forEach(e1 -> {
                    if(userCommentCount.containsKey(e1.getValue().getUserId()))
                        userCommentCount.put(e1.getValue().getUserId(), userCommentCount.get(e1.getValue().getUserId()) +1);
                    else{
                        userCommentCount.put(e1.getValue().getUserId(), 1);
                    }
                });
        
        Map<Integer,Integer> sortedUserCommentCount = userCommentCount.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer,Integer>comparingByValue())
                .collect(toMap(Map.Entry::getKey,
                                Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
        
        System.out.println(sortedUserCommentCount);
        List<Integer> userList = new ArrayList<>(sortedUserCommentCount.keySet());
        System.out.println("Five most Inactive user by create comments number: ");
        for(int i=0;i<5;i++){
            System.out.print("#" + (i+1) +" UserId: " + userList.get(i) + " --- ");
            System.out.println(DataStore.getInstance().getUsers().get(userList.get(i)));
        }
        
    }
    
    // @author Shalini Chandra
    //***********************Top 5 inactive users overall (sum of comments, posts and likes)********************//
    
       public void getFiveInactiveUsersByOverall(){
        Map<Integer,Integer> userProactiveCount = generateUsersProactiveMap();
       
        List<Map.Entry<Integer,Integer>> sortedMapList = MapSorter.sortMapByValueAscending(userProactiveCount);
        System.out.println("Top 5 Inactive users overall:");
        for(int i = 0;i<5;i++){
            Integer userID = sortedMapList.get(i).getKey();
            Integer proactiveCount = sortedMapList.get(i).getValue();
            System.out.printf("%d. user ID: %d ; active count: %d \n",i+1,userID,proactiveCount);
        }
    }
    
       // @author Shalini Chandra
    //***********************Top 5 proactive users overall (sum of comments, posts and likes)********************//
    
       
        public void getFiveProactiveUsersByOverall(){
       Map<Integer,Integer> userProactiveCount = generateUsersProactiveMap();
       
       List<Map.Entry<Integer,Integer>> sortedMapList = MapSorter.sortMapByValueDescend(userProactiveCount);
       System.out.println("Top 5 proactive users overall:");
       for(int i = 0;i<5;i++){
           Integer userID = sortedMapList.get(i).getKey();
           Integer proactiveCount = sortedMapList.get(i).getValue();
           System.out.printf("%d. user ID: %d ; active count: %d \n",i+1,userID,proactiveCount);
       }
       
    }
    
    
}
