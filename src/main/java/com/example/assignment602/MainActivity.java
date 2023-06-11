package com.example.assignment602;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText nounitUsed;
    EditText Rebates;
    Button Calculate;
    TextView FinalCharges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nounitUsed = findViewById(R.id.unitUsed);
        Rebates = findViewById(R.id.editTextRebate);
        Calculate = findViewById(R.id.buttonCalculate);
        FinalCharges = findViewById(R.id.tvFinalCharges);

        Calculate.setOnClickListener(this);

        Button buttonReset = findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });

        ImageView imageViewHelp = findViewById(R.id.imageViewHelp);
        imageViewHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescriptionDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.about) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCalculate:
                if (nounitUsed.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please enter a value for units used", Toast.LENGTH_SHORT).show();
                    return;
                }

                int unitsUsed = Integer.parseInt(nounitUsed.getText().toString());
                double totalCharges;

                if (unitsUsed <= 200) {
                    totalCharges = unitsUsed * 0.218;
                } else if (unitsUsed <= 300) {
                    totalCharges = 200 * 0.218 + (unitsUsed - 200) * 0.334;
                } else if (unitsUsed <= 600) {
                    totalCharges = 200 * 0.218 + 100 * 0.334 + (unitsUsed - 300) * 0.516;
                } else {
                    totalCharges = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (unitsUsed - 600) * 0.546;
                }

                EditText etRebate = findViewById(R.id.editTextRebate);
                String rebateValue = etRebate.getText().toString().trim();
                double rebatePercentage;

                if (rebateValue.isEmpty()) {
                    Toast.makeText(this, "Please enter a value for rebate", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    rebatePercentage = Double.parseDouble(rebateValue);
                }

                if (rebatePercentage < 0 || rebatePercentage > 5) {
                    Toast.makeText(this, "Please enter a rebate percentage between 0% and 5%", Toast.LENGTH_SHORT).show();
                    return;
                }

                double rebateAmount = totalCharges * (rebatePercentage / 100.0);
                double finalCharges = totalCharges - rebateAmount;

                FinalCharges.setText("Total charges: " + totalCharges + "\nRebate amount: " + rebateAmount + "\nFinal charges: " + finalCharges);
                break;
        }
    }

    private void clearFields() {
        nounitUsed.setText("");
        Rebates.setText("");
        FinalCharges.setText("");
    }

    private void showDescriptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Calculator Description");
        builder.setMessage("This calculator helps you calculate the final charges based on the unit used and rebate percentage.\n\n" +
                "1. Enter the unit used in kilowatt-hours (kWh) in the 'Unit Used' field.\n\n" +
                "2. Enter the rebate percentage in the 'Rebate' field. For example, the rebate percentage range is 0-5%, so enter 0 until 5 only.\n\n" +
                "3. Click on the 'Calculate' button to calculate the final charges.\n\n" +
                "4. Click on the 'Reset' button to clear the input fields.\n\n" +
                "The calculated final charges will be displayed below the buttons.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
