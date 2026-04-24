package kh.rupp.edu.previewlab;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("tasks")
    Call<List<Task>> getTasks();
}
