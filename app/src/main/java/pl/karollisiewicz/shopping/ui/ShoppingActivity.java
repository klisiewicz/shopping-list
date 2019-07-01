package pl.karollisiewicz.shopping.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;

import pl.karollisiewicz.shopping.R;

import static androidx.navigation.ActivityKt.findNavController;
import static androidx.navigation.ui.ActivityKt.setupActionBarWithNavController;
import static androidx.navigation.ui.NavControllerKt.navigateUp;

public class ShoppingActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping);
        setSupportActionBar(this.<Toolbar>findViewById(R.id.toolbar));
        setupAppBarConfiguration();
        setupActionBarWithNavController(this,
                findNavController(this, R.id.navHostFragment),
                appBarConfiguration
        );
    }

    private void setupAppBarConfiguration() {
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.shoppingListFragment).build();
    }

    @Override
    public boolean onSupportNavigateUp() {
        final NavController navController = findNavController(this, R.id.navHostFragment);
        return navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
