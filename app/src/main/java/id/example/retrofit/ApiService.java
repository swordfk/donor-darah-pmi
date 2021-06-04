package id.example.retrofit;

import java.util.List;

import id.example.model.DaftarDonor;
import id.example.model.Login;
import id.example.model.Pendonor;
import id.example.model.PostKPMI;
import id.example.model.User;
import id.example.retrofit.response.Darah;
import id.example.retrofit.response.Jadwal;
import id.example.retrofit.response.KpmiResponse;
import id.example.retrofit.response.LoginResponse;
import id.example.retrofit.response.MessageResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> loginAdmin(
            @Body Login login);
    @POST("registrasi")
    Call<MessageResponse> registrasi(
            @Body User user);
    @GET("kpmi")
    Call<List<KpmiResponse>> listKpmi(
            @Header("Authorization") String token);
    @POST("kmpi")
    Call<MessageResponse> createKpmi(
            @Header("Authorization") String token,
            @Body KpmiResponse kpmiResponse);
    @POST("darah")
    Call<MessageResponse> addDarah(
            @Header("Authorization") String token,
            @Body Darah darah);
    @POST("darah/edit")
    Call<MessageResponse> updateStok(
            @Header("Authorization") String token,
            @Body Darah darah);
    @POST("jadwal/add")
    Call<MessageResponse> addJadwal(
            @Header("Authorization") String token,
            @Body Jadwal jadwal);
    @POST("jadwal")
    Call<List<Jadwal>> getJadwal(
            @Header("Authorization") String token,
            @Query("id") int id);
    @POST("kpmi")
    Call<MessageResponse> addKPMI(
            @Header("Authorization") String token,
            @Body PostKPMI postKPMI);
    @POST("pendonor/add")
    Call<MessageResponse> daftarDonor(
            @Header("Authorization") String token,
            @Body DaftarDonor daftarDonor);
    @GET("pendonor")
    Call<List<Pendonor>> getPendonor(
            @Header("Authorization") String token);
}
