package com.application.getgoproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.getgoproject.adapter.CommentAdapter;
import com.application.getgoproject.adapter.ImageLocationAdapter;
import com.application.getgoproject.callback.LocationCallback;
import com.application.getgoproject.callback.LocationCommentCallback;
import com.application.getgoproject.callback.UserCallback;
import com.application.getgoproject.dto.CommentDTO;
import com.application.getgoproject.models.ImageLocation;
import com.application.getgoproject.models.LocationComment;
import com.application.getgoproject.models.Locations;
import com.application.getgoproject.models.User;
import com.application.getgoproject.models.UserAuthentication;
import com.application.getgoproject.service.CommentService;
import com.application.getgoproject.service.LocationService;
import com.application.getgoproject.service.UserService;
import com.application.getgoproject.utils.RetrofitClient;
import com.application.getgoproject.utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailLocationActivity extends AppCompatActivity {

    private LocationService locationService;
    private CommentService commentService;
    private UserService userService;
    private UserAuthentication userAuthentication;
    private String userToken;
    private String locationCity;
    private int locationId;
    private String userName;

    private ListView lvComment;
    private ArrayList<LocationComment> arrayComment;
    private CommentAdapter adapter;
    private ImageButton imgbtnGoback;
    private TextView etNameLocation, contentDescription, address, price, tvShowMore, ratingOverall, reviewCount;
    private ProgressBar progress5Star, progress4Star, progress3Star, progress2Star, progress1Star;
    private RatingBar ratingBarOverall, feedBackRating;
    private ArrayList<ImageLocation> arrayImage;
    private ImageLocationAdapter imageAdapter;
    private RecyclerView recyclerImage;
    private Button btnComment;
    private TextInputEditText inputComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_local);

        userAuthentication = SharedPrefManager.getInstance(this).getUser();
        userToken = "Bearer " + userAuthentication.getAccessToken();

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(this);
        locationService = retrofit.create(LocationService.class);
        commentService = retrofit.create(CommentService.class);
        userService = retrofit.create(UserService.class);

        anhXa();

        Intent intent = getIntent();
        locationId = intent.getIntExtra("detail location", 0);

        imageAdapter = new ImageLocationAdapter(this, R.layout.layout_img_location, arrayImage);
        recyclerImage.setAdapter(imageAdapter);
        recyclerImage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        adapter = new CommentAdapter(this, R.layout.layout_cmt, arrayComment);
        lvComment.setAdapter(adapter);

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapForm();
            }
        });
        imgbtnGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getLocationById(locationId, userToken, new LocationCallback() {
            @Override
            public void onLocationsFetched(List<Locations> locations) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onOneLocationsFetched(Locations locations) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        locationCity = locations.getCity();
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayImage.clear();
                        if (locations.getImages() != null) {
                            for (String images : locations.getImages()) {
                                if (images != null && !images.isEmpty()) {
                                    arrayImage.add(new ImageLocation(images));
                                } else {
                                    arrayImage.add(new ImageLocation(""));
                                }
                            }
                        }
                        else {
                            arrayImage.add(new ImageLocation(""));
                        }
                        imageAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        getUserByUsername(userAuthentication.getUsername(), userToken, new UserCallback() {
            @Override
            public void onUserFetched(User user) {
                userName = user.getUserName();
            }
        });

        tvShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvShowMore.getText().toString().equalsIgnoreCase("Show more")) {
                    contentDescription.setMaxLines(Integer.MAX_VALUE);
                    tvShowMore.setText("Show less");
                }
                else {
                    contentDescription.setMaxLines(10);
                    tvShowMore.setText("Show more");
                }
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentText = inputComment.getText().toString().trim();
                float ratingNumber = feedBackRating.getRating();

                List<String> images = new ArrayList<>();
                CommentDTO commentDTO = new CommentDTO(commentText, images, ratingNumber, userName, locationId);
                sendComment(commentDTO, userToken);
            }
        });

        getLocationCommentById(locationId, userToken, new LocationCommentCallback() {
            @Override
            public void onLocationCommentsFetched(List<LocationComment> locationComments) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayComment.clear();

                        for (LocationComment locationComment : locationComments) {
                            arrayComment.add(new LocationComment(locationComment.getContent(), locationComment.getImages(),
                                    locationComment.getRating(), locationComment.getUserId(),
                                    locationComment.getCreatedDate(), locationComment.getLocation()));
                        }

                        // Update average rating and progress bars
                        float averageRating = LocationComment.calculateAverageRating(locationComments);
                        ratingOverall.setText(String.format("%.1f", averageRating));
                        ratingBarOverall.setRating(averageRating);
                        updateRatingProgressBars(locationComments);

                        reviewCount.setText(arrayComment.size() + " reviews");
                        Log.d("Comment Data", arrayComment.size() + "");
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    private void anhXa(){
        etNameLocation = findViewById(R.id.etNameLocation);
        contentDescription = findViewById(R.id.contentDescription);
        imgbtnGoback = findViewById(R.id.imgbtnGoback);
        recyclerImage = findViewById(R.id.recyclerImage);
        lvComment = (ListView) findViewById(R.id.lvComment);
        address = findViewById(R.id.address);
        price = findViewById(R.id.price);
        tvShowMore = findViewById(R.id.tvShowMore);
        btnComment = findViewById(R.id.btnComment);
        inputComment = findViewById(R.id.inputComment);
        ratingOverall = findViewById(R.id.ratingOverall);
        progress5Star = findViewById(R.id.progress5Star);
        progress4Star = findViewById(R.id.progress4Star);
        progress3Star = findViewById(R.id.progress3Star);
        progress2Star = findViewById(R.id.progress2Star);
        progress1Star = findViewById(R.id.progress1Star);
        ratingBarOverall = findViewById(R.id.ratingBarOverall);
        feedBackRating = findViewById(R.id.feedbackRating);
        reviewCount = findViewById(R.id.reviewCount);

        feedBackRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v > 0) {
                    btnComment.setEnabled(true);
                    inputComment.setEnabled(true);
                }
                else {
                    btnComment.setEnabled(false);
                    inputComment.setEnabled(false);
                }
            }
        });

        arrayImage = new ArrayList<>();


        arrayComment = new ArrayList<>();
    }

    private void mapForm(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("location address", locationId);
        startActivity(intent);
    }

    private void getLocationById(int id, String token, LocationCallback callback) {
        Call<Locations> call = locationService.getLocationsById(id, token);
        call.enqueue(new Callback<Locations>() {
            @Override
            public void onResponse(Call<Locations> call, Response<Locations> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Locations locations = response.body();

                    etNameLocation.setText(locations.getName());
                    contentDescription.setText(locations.getShortDescription());
                    address.setText(locations.getAddress());
                    price.setText(locations.getPrice());

                    arrayImage.clear();
                    if (locations.getImages() != null) {
                        for (String images : locations.getImages()) {
                            if (images != null && !images.isEmpty()) {
                                arrayImage.add(new ImageLocation(images));
                            } else {
                                arrayImage.add(new ImageLocation("")); // Placeholder for default image handling
                            }
                        }
                    }
                    else {
                        arrayImage.add(new ImageLocation(""));
                    }
                    imageAdapter.notifyDataSetChanged();

                    callback.onOneLocationsFetched(locations);
                }
                else {
                    Log.d("Error", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Locations> call, Throwable throwable) {
                Log.d("Error", throwable.getMessage());
            }
        });
    }

    public void getUserByUsername(String username, String token, UserCallback callback) {
        try {
            Call<User> call = userService.getUserByUsername(username, token);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();
                        callback.onUserFetched(user);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable throwable) {
                    // Handle failure
                }
            });
        }
        catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    private void sendComment(CommentDTO commentDTO, String token) {
        try {
            Log.d("Sending Comment", "CommentDTO: " + commentDTO.toString());

            Call<ResponseBody> call = commentService.createComments(commentDTO, token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseString = response.body().string();
                            Log.d("Comment Created", "Response: " + responseString);

                            // Assuming success message is "Action success", you can handle this as needed
                            if (responseString.equals("Action success")) {
                                // Handle success, e.g., update UI, fetch comments again, etc.
                                getLocationCommentById(locationId, userToken, new LocationCommentCallback() {
                                    @Override
                                    public void onLocationCommentsFetched(List<LocationComment> locationComments) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                arrayComment.clear();
                                                arrayComment.addAll(locationComments);

                                                // Update the average rating and progress bars
                                                float averageRating = LocationComment.calculateAverageRating(locationComments);
                                                ratingOverall.setText(String.format("%.1f", averageRating));
                                                ratingBarOverall.setRating(averageRating);
                                                updateRatingProgressBars(locationComments);

                                                reviewCount.setText(arrayComment.size() + " reviews");
                                                adapter.notifyDataSetChanged();

                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        Log.e("Error", "Error fetching location comments: " + throwable.getMessage());
                                    }
                                });

                                inputComment.setText("");
                                feedBackRating.setRating(0);
                            } else {
                                // Handle error, e.g., show toast or log error
                                Log.e("Error", "Unexpected response: " + responseString);
                            }
                        } catch (IOException e) {
                            Log.e("Error", "Failed to read response body: " + e.getMessage(), e);
                        }
                    } else {
                        Log.e("Error", "Failed to create comment. Response code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    Log.e("Error", "Failed to create comment. Error message: " + throwable.getMessage(), throwable);
                }
            });
        } catch (Exception e) {
            Log.e("Error", "Exception while creating comment: " + e.getMessage(), e);
        }
    }


    private void getLocationCommentById(int id, String token, LocationCommentCallback callback) {
        try {
            Call<List<LocationComment>> call = locationService.getListLocationCommentById(id, token);
            call.enqueue(new Callback<List<LocationComment>>() {
                @Override
                public void onResponse(Call<List<LocationComment>> call, Response<List<LocationComment>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<LocationComment> locationComments = response.body();

                        // Update average rating and progress bars
                        float averageRating = LocationComment.calculateAverageRating(locationComments);
                        ratingOverall.setText(String.format("%.1f", averageRating));
                        ratingBarOverall.setRating(averageRating);
                        updateRatingProgressBars(locationComments);

                        ratingOverall.setText(String.format("%.1f", averageRating));
                        ratingBarOverall.setRating(averageRating);

                        callback.onLocationCommentsFetched(locationComments);
                    }
                }

                @Override
                public void onFailure(Call<List<LocationComment>> call, Throwable throwable) {
                    Log.d("Failed", throwable.getMessage());
                }
            });
        }
        catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    private void updateRatingProgressBars(List<LocationComment> locationComments) {
        int[] distribution = LocationComment.calculateRatingDistribution(locationComments);
        int totalComments = locationComments.size();

        progress5Star.setMax(totalComments);
        progress4Star.setMax(totalComments);
        progress3Star.setMax(totalComments);
        progress2Star.setMax(totalComments);
        progress1Star.setMax(totalComments);

        progress5Star.setProgress(distribution[4]);
        progress4Star.setProgress(distribution[3]);
        progress3Star.setProgress(distribution[2]);
        progress2Star.setProgress(distribution[1]);
        progress1Star.setProgress(distribution[0]);
    }
}
