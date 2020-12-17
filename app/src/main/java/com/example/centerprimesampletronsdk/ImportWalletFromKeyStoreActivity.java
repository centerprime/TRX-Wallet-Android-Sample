package com.example.centerprimesampletronsdk;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.centerprime.tronsdk.sdk.TronWalletManager;
import com.example.centerprimesampletronsdk.databinding.ActivityImportWalletKeystoreBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImportWalletFromKeyStoreActivity extends AppCompatActivity {

    ActivityImportWalletKeystoreBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_import_wallet_keystore);

        /**
         * Using this importFromKeyStore function user can import his wallet from keystore.
         *
         * @params keystore, password, Context
         *
         * @return walletAddress
         */

        TronWalletManager tronWalletManager = TronWalletManager.getInstance();
        tronWalletManager.init(this);


        binding.importBtn.setOnClickListener(v -> {
            String password = binding.password.getText().toString();
            String keystore = binding.keystoreT.getText().toString();
            tronWalletManager.importByKeystore(password, keystore, this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(walletAddress -> {

                        binding.walletAddress.setText(walletAddress.getAddress());
                        binding.copy.setVisibility(View.VISIBLE);
                       // Toast.makeText(this, "Wallet Address : " + walletAddress, Toast.LENGTH_SHORT).show();

                    }, error -> {
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println("** ** ** ** "+error.getMessage());
                    });
        });

        binding.copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.walletAddress.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
        });
    }
}
