package com.universitaskuningan.yamaha_dealer_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.universitaskuningan.yamaha_dealer_app.databinding.ActivityMainBinding;
import com.universitaskuningan.yamaha_dealer_app.databinding.ListItemBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ArrayList<ItemModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the ViewBinding object
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inisialisasi RecyclerView menggunakan ViewBinding
        binding.recyclerView.setHasFixedSize(true);

        // Mengatur LayoutManager
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(recyclerViewLayoutManager);

        // Menambahkan garis pembatas
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Mengisi data
        data = new ArrayList<>();
        for (int i = 0; i < Myitem.motorTypes.length; i++) {
            // Mengambil nama motor dari string resource
            String motorName = Myitem.motorTypes[i];
            int motorImage = Myitem.motorImages[i];

            // Menambahkan motor dan deskripsinya
            data.add(new ItemModel(motorName, motorImage));
        }

        // Inisialisasi Adapter
        adapter = new AdapterRecycleView(data);
        binding.recyclerView.setAdapter(adapter);

        setupBottomNavigation();
        setupSearch();
    }


    // Tambahkan metode ini untuk mengatur fitur pencarian
    private void setupSearch() {
        // Tangani perubahan teks secara real-time
        binding.searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak diperlukan
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Panggil filter saat teks berubah
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Tidak diperlukan
            }
        });

        // Tangani klik tombol pencarian
        binding.searchButton.setOnClickListener(v -> {
            String query = binding.searchInput.getText().toString();
            if (!query.isEmpty()) {
                filterData(query);
            } else {
                Toast.makeText(this, "Masukkan kata kunci untuk mencari", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Tambahkan metode untuk memfilter data
// Logika untuk memfilter data berdasarkan input pencarian
    private void filterData(String query) {
        ArrayList<ItemModel> filteredList = new ArrayList<>();

        // Periksa setiap item apakah cocok dengan query
        for (ItemModel item : data) {
            if (item.getMotorType().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

        // Update RecyclerView dengan hasil pencarian
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Tidak ada hasil ditemukan", Toast.LENGTH_SHORT).show();
        }

        // Set data baru ke adapter dan refresh RecyclerView
        adapter = new AdapterRecycleView(filteredList);
        binding.recyclerView.setAdapter(adapter);
    }


    @SuppressLint("NonConstantResourceId")
    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_profile) {
                // Buka ProfileActivity
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });

    }

    // Menambahkan logika untuk mengirimkan data ke activity_detail saat item di RecyclerView diklik
    private class AdapterRecycleView extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<ItemModel> itemModels;

        public AdapterRecycleView(ArrayList<ItemModel> itemModels) {
            this.itemModels = itemModels;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemBinding binding = ListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ItemModel item = itemModels.get(position);
            holder.bind(item, currentItem -> {
                // Mengirimkan data motor ke activity_detail
                Intent intent = new Intent(holder.itemView.getContext(), activity_detail.class);
                intent.putExtra("MOTOR_NAME", currentItem.getMotorType());
                intent.putExtra("MOTOR_IMAGE", currentItem.getMotorImage());
                intent.putExtra("MOTOR_DESCRIPTION", getMotorDescription(currentItem.getMotorType())); // Deskripsi motor
                intent.putExtra("MOTOR_SPECIFICATIONS", getMotorSpecifications(currentItem.getMotorType())); // Spesifikasi motor
                holder.itemView.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return itemModels.size();
        }

        // Mendapatkan deskripsi motor
        private String getMotorDescription(String motorType) {
            switch (motorType) {
                case "Kucing Persia":
                    return "Kucing berbulu panjang dengan wajah bulat dan hidung pesek. Memiliki mata besar dan ekspresi lembut.";
                case "mainecoon":
                    return "Salah satu ras kucing terbesar dengan bulu lebat dan ekor panjang berbulu tebal. Memiliki mata tajam dan telinga berjumbai.";
                case "siamese":
                    return "Tubuh ramping dengan bulu pendek berwarna krem atau putih dan pola gelap pada telinga, wajah, ekor, dan kaki. Mata biru cerah adalah ciri khasnya.";
                case "bengal":
                    return "Kucing berbulu pendek dengan pola berbintik atau bergaris menyerupai macan tutul. Tubuh berotot dan atletis.";
                case "ragdoll":
                    return "Berbulu panjang, tubuh besar, dan mata biru yang indah. Cenderung memiliki pola warna mirip Siamese tetapi lebih lembut.";
                case "scottishfold":
                    return "Telinga melipat ke depan adalah ciri khasnya. Tubuh medium dengan bulu pendek atau panjang.";
                case "sphynx":
                    return "Tidak memiliki bulu (meski sebenarnya memiliki lapisan rambut halus), kulit keriput, dan telinga besar.";
                default:
                    return "Deskripsi tidak tersedia.";
            }
        }

        // Mendapatkan spesifikasi motor
        private String getMotorSpecifications(String motorType) {
            switch (motorType) {
                case "Kucing Persia":
                    return "Iran (dahulu Persia).";
                case "mainecoon":
                    return "Amerika Serikat (Maine).";
                case "siamese":
                    return "Thailand (dulu dikenal sebagai Siam)";
                case "bengal":
                    return "Amerika Serikat (hasil persilangan kucing domestik dan Asian Leopard Cat).";
                case "ragdoll":
                    return "Amerika Serikat.";
                case "scottishfold":
                    return "Skotlandia.";
                case "sphynx":
                    return "Kanada.";
                default:
                    return "Spesifikasi tidak tersedia.";
            }
        }
    }

    // ViewHolder sebagai kelas terpisah
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemBinding binding;

        public ViewHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ItemModel item, ViewHolderClickListener clickListener) {
            binding.motorType.setText(item.getMotorType());
            binding.motorImage.setImageResource(item.getMotorImage());

            // Klik untuk membuka activity_detail
            binding.getRoot().setOnClickListener(v -> clickListener.onClick(item));
        }
    }

    // Listener untuk menangani klik item
    public interface ViewHolderClickListener {
        void onClick(ItemModel item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;  // Prevent memory leaks
    }
}
