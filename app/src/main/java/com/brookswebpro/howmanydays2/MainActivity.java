package com.brookswebpro.howmanydays2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] monthsArray = {"January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"};
    ArrayList<Integer> daysArray = new ArrayList<Integer>();
    Spinner spinner;
    ArrayAdapter<String> adapter;
    RelativeLayout layoutR;
    EditText yearEntered;
    TextView resultTextView;
    TextView label;
    ImageView whiteLabel;
    Button enterButton;
    Button startOverButton;
    boolean isMonthSelected = false;
    boolean isDaySelected = false;
    boolean isYearSelected = false;
    String month;
    String day;
    String year;
    String date;
    int daysToAdd;
    int monthNumber;
    int addMonthDays;
    int dayNumber;
    int yearNumber;
    int yearNow;
    int monthNow;
    int dayNow;
    String dateString;

    public void submitPressed (View view) {

        if (!isMonthSelected) {
            isMonthSelected = true;
            date = month;
            switch (month) {
                case "January":
                    addDaysToMonth(2);
                    monthNumber = 1;
                    addMonthDays = 0;
                    break;
                case "February":
                    monthNumber = 2;
                    addMonthDays = 31;
                    break;
                case "March":
                    addDaysToMonth(2);
                    monthNumber = 3;
                    addMonthDays = 59;
                    break;
                case "April":
                    addDaysToMonth(1);
                    monthNumber = 4;
                    addMonthDays = 90;
                    break;
                case "May":
                    addDaysToMonth(2);
                    monthNumber = 5;
                    addMonthDays = 120;
                    break;
                case "June":
                    addDaysToMonth(1);
                    monthNumber = 6;
                    addMonthDays = 151;
                    break;
                case "July":
                    addDaysToMonth(2);
                    monthNumber = 7;
                    addMonthDays = 181;
                    break;
                case "August":
                    addDaysToMonth(2);
                    monthNumber = 8;
                    addMonthDays = 212;
                    break;
                case "September":
                    addDaysToMonth(1);
                    monthNumber = 9;
                    addMonthDays = 243;
                    break;
                case "October":
                    addDaysToMonth(2);
                    monthNumber = 10;
                    addMonthDays = 273;
                    break;
                case "November":
                    addDaysToMonth(1);
                    monthNumber = 11;
                    addMonthDays = 303;
                    break;
                case "December":
                    addDaysToMonth(2);
                    monthNumber = 12;
                    addMonthDays = 334;
                    break;
            }
            ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(this, R.layout.spinner_item, daysArray);
//            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter2.setDropDownViewResource(R.layout.spinner_item);
            spinner.setAdapter(adapter2);
            resultTextView.setText("You entered: " + date);
            label.setText("Day: ");
            enterButton.setText("Submit Day");


        } else if (!isDaySelected) {
            isDaySelected = true;
            date += " " + day;
            dayNumber = Integer.parseInt(day);
            //activate edit text field
            yearEntered.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            resultTextView.setText("You entered: " + date + "\n\n ENTER YEAR");
            label.setText("Year: ");
            enterButton.setText("Submit Year");

        } else if (!isYearSelected){
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(yearEntered.getWindowToken(), 0);
            if (TextUtils.isEmpty(yearEntered.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Please enter a number", Toast.LENGTH_LONG).show();
                yearEntered.setText("");
            } else {
                try {
                    year = yearEntered.getText().toString();
                    yearNumber = Integer.parseInt(year);
                    if (yearNumber > 1000000) {
                        Toast.makeText(getApplicationContext(), "Please enter a smaller number", Toast.LENGTH_LONG).show();
                        yearEntered.setText("");
                    } else if (((monthNumber == 2 && dayNumber == 29) && (((yearNumber - 2000) % 4 != 0) || ((yearNumber - 2000) % 100 == 0) && ((yearNumber - 2000) % 400 != 0)))) {
                        Toast.makeText(getApplicationContext(), year + " is not a leap year. Please try again.", Toast.LENGTH_LONG).show();
                        resetApp();
                    } else {
                        date += ", " + yearEntered.getText().toString();
                        resultTextView.setText("You entered: " + date);
                        yearEntered.setText("");
                        yearEntered.setVisibility(View.INVISIBLE);
                        whiteLabel.setVisibility(View.INVISIBLE);
                        label.setVisibility(View.INVISIBLE);
                        enterButton.setText("How Many Days?");
                        isYearSelected = true;
                    }
                } catch (Exception e) {
                    resultTextView.setText("Please enter a smaller number");
                    yearEntered.setText("");
                }
            }
        } else {
            Calendar now = Calendar.getInstance();
            yearNow = now.get(Calendar.YEAR);
            monthNow = now.get(Calendar.MONTH) + 1;
            dayNow = now.get(Calendar.DAY_OF_MONTH);
            dateString = String.valueOf(monthNow) + " " + String.valueOf(dayNow) + ", " + String.valueOf(yearNow);
            enterButton.setVisibility(View.INVISIBLE);
            howManyDays(monthNow, dayNow, yearNow);

        }
    }

    public void howManyDays (int monthNow, int dayNow, int yearNow) {

        boolean dayIsFuture = false;
        int totalDifference = 0;
        int leapDays = 0;
        int yearDifference = 0;
        int monthDifference = 0;
        int dayDifference = 0;
        int adjustDays = 0;
        int adjustMonth = 0;

        if (yearNumber > yearNow) {
            dayIsFuture = true;
            yearDifference = yearNumber - yearNow - 1;
            if (yearDifference >= 2) {
                leapDays = calculateLeapDays(yearNow + 1, yearNumber - 1);
            } else if (yearDifference == 1) {
                if ((yearNow + 1) % 4 == 0) {
                    leapDays += 1;
                    if (((yearNow + 1) % 100 == 0) && ((yearNow + 1) % 400 != 0)) {
                        leapDays -= 1;
                    }
                }
            }
            if (monthNumber > monthNow) {
                yearDifference += 1;
                monthDifference = monthNumber - monthNow - 1;
                if (dayNumber >= dayNow) {
                    monthDifference += 1;
                    dayDifference = dayNumber - dayNow;
                    adjustMonth = monthNow;
                    while (adjustMonth < monthNumber) {
                        adjustDays += adjustDaysOfMonth (adjustMonth, yearNow);
                        adjustMonth += 1;
                    }
                } else {
                    dayDifference = 30 - dayNow + dayNumber;
                    dayDifference += adjustDaysOfMonth (monthNow, yearNow);
                    adjustMonth = monthNow + 1;
                    while (adjustMonth < monthNow) {
                        adjustDays += adjustDaysOfMonth (adjustMonth, yearNow);
                        adjustMonth += 1;
                    }
                }
            } else if (monthNumber < monthNow) {
                //START BACK HERE
                monthDifference =  12 - monthNow + monthNumber - 1;
                if (dayNumber >= dayNow) {
                    monthDifference += 1;
                    dayDifference = dayNumber - dayNow;
                    adjustMonth = monthNow;
                    while (adjustMonth <= 12) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNow);
                        adjustMonth += 1;
                    }
                    adjustMonth = 1;
                    while (adjustMonth < monthNumber) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNumber);
                        adjustDays += 1;
                    }
                } else {
                    dayDifference = 30 - dayNow + dayNumber;
                    dayDifference += adjustDaysOfMonth(monthNow, yearNow);
                    adjustMonth = monthNow + 1;
                    while (adjustMonth <= 12) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNow);
                        adjustMonth += 1;
                    }
                    adjustMonth = 1;
                    while (adjustMonth < monthNumber) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNumber);
                        adjustMonth += 1;
                    }
                }
            } else {
                if (dayNumber >= dayNow) {
                    monthDifference = 0;
                    yearDifference += 1;
                    dayDifference = dayNumber - dayNow;
                } else {
                    monthDifference = 12 - monthNow + monthNumber - 1;
                    dayDifference = 30 - dayNow + dayNumber;
                    dayDifference += adjustDaysOfMonth(monthNow, yearNow);
                    adjustMonth = monthNow + 1;
                    while (adjustMonth <= 12) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNow);
                        adjustMonth += 1;
                    }
                    adjustMonth = 1;
                    while (adjustMonth < monthNumber) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNumber);
                        adjustMonth += 1;
                    }
                }
            }
        } else if (yearNumber < yearNow) {
            dayIsFuture = false;
            yearDifference = yearNow - yearNumber - 1;
            if (yearDifference >= 2) {
                leapDays = calculateLeapDays(yearNumber + 1, yearNow - 1);
            } else if (yearDifference == 1) {
                if ((yearNumber + 1) % 4 == 0) {
                    leapDays += 1;
                    if (((yearNumber + 1) % 100 == 0) && ((yearNumber + 1) % 400 != 0)) {
                        leapDays -= 1;
                    }
                }
            }
            if (monthNow > monthNumber) {
                yearDifference += 1;
                monthDifference = monthNow - monthNumber -1;
                if (dayNow >= dayNumber) {
                    monthDifference += 1;
                    dayDifference = dayNow - dayNumber;
                    adjustMonth = monthNumber;
                    while (adjustMonth < monthNow) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNumber);
                        adjustMonth += 1;
                    }
                } else {
                    dayDifference = 30 - dayNumber + dayNow;
                    dayDifference += adjustDaysOfMonth(monthNumber, yearNumber);
                    adjustMonth = monthNumber + 1;
                    while (adjustMonth < monthNow) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNumber);
                        adjustMonth += 1;
                    }
                }
            } else if (monthNow < monthNumber) {
                monthDifference = 12 - monthNumber + monthNow - 1;
                if (dayNow >= dayNumber) {
                    monthDifference += 1;
                    dayDifference = dayNow - dayNumber;
                    adjustMonth = monthNumber;
                    while (adjustMonth <= 12) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNumber);
                        adjustMonth += 1;
                    }
                    adjustMonth = 1;
                    while (adjustMonth < monthNow) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNow);
                        adjustMonth += 1;
                    }
                } else {
                    dayDifference = 30 - dayNumber + dayNow;
                    dayDifference += adjustDaysOfMonth(monthNumber, yearNumber);
                    adjustMonth = monthNumber + 1;
                    while (adjustMonth <= 12) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNumber);
                        adjustMonth += 1;
                    }
                    adjustMonth = 1;
                    while (adjustMonth < monthNow) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNow);
                        adjustMonth += 1;
                    }
                }
            } else {
                if (dayNow >= dayNumber) {
                    monthDifference = 0;
                    yearDifference += 1;
                    dayDifference = dayNow - dayNumber;
                } else {
                    monthDifference = 12 - monthNumber + monthNow -1;
                    dayDifference = 30 - dayNumber + dayNow;
                    dayDifference += adjustDaysOfMonth(monthNumber, yearNumber);
                    adjustMonth = monthNumber + 1;
                    while (adjustMonth <= 12) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNumber);
                        adjustMonth += 1;
                    }
                    adjustMonth = 1;
                    while (adjustMonth < monthNow) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNow);
                        adjustMonth += 1;
                    }
                }
            }
        } else {
            if (monthNumber > monthNow) {
                dayIsFuture = true;
                monthDifference = monthNumber - monthNow- 1;
                if (dayNumber >= dayNow) {
                    monthDifference += 1;
                    dayDifference = dayNumber - dayNow;
                    adjustMonth = monthNow;
                    while (adjustMonth < monthNumber) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNow);
                        adjustMonth += 1;
                    }
                } else {
                    dayDifference = 30 - dayNow + dayNumber;
                    dayDifference += adjustDaysOfMonth(monthNow, yearNow);
                    adjustMonth = monthNow + 1;
                    while (adjustMonth < monthNumber) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNow);
                        adjustMonth += 1;
                    }
                }
            } else if (monthNumber < monthNow) {
                dayIsFuture = false;
                monthDifference = monthNow - monthNumber - 1;
                if (dayNow >= dayNumber) {
                    monthDifference += 1;
                    dayDifference = dayNow - dayNumber;
                    adjustMonth = monthNumber;
                    while (adjustMonth < monthNow) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNumber);
                        adjustMonth += 1;
                    }
                } else {
                    dayDifference = 30 - dayNumber + dayNow;
                    dayDifference += adjustDaysOfMonth(monthNumber, yearNumber);
                    adjustMonth = monthNumber + 1;
                    while (adjustMonth < monthNow) {
                        adjustDays += adjustDaysOfMonth(adjustMonth, yearNumber);
                        adjustMonth += 1;
                    }
                }
            } else {
                if (dayNumber > dayNow) {
                    dayIsFuture = true;
                    dayDifference = dayNumber - dayNow;
                } else if (dayNumber < dayNow) {
                    dayIsFuture = false;
                    dayDifference = dayNow - dayNumber;
                } else {
                    dayIsFuture = false;
                }
            }
        }
        totalDifference = (yearDifference * 365) + (monthDifference * 30) + dayDifference + leapDays + adjustDays;
        String message = date;

        if (dayIsFuture) {
            message += " is ";
        } else {
            message += " was ";
        }

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        message += formatter.format(yearDifference) + " year(s), ";
        message += monthDifference + " month(s), and ";
        message += dayDifference + " day(s) ";

        if (dayIsFuture) {
            message += " from now";
        } else {
            message += " ago";
        }

        message += ", for a TOTAL of " + formatter.format(totalDifference) + " day(s)";
        resultTextView.setText(message);
        startOverButton.setVisibility(View.VISIBLE);

    }

    public int adjustDaysOfMonth (int month, int year) {
        int returnNumber = 0;
        switch (month) {
            case 1:case 3:case 5:case 7:case 8:case 10:case 12:
                returnNumber = 1;
                break;
            case 4:case 6:case 9:case 11:
                returnNumber = 0;
                break;
            case 2:
                if (year % 4 == 0) {
                    returnNumber = -1;
                } else {
                    returnNumber = -2;
                }
        }

        return returnNumber;
    }

    public void addDaysToMonth (int daysToAdd) {
        for (int i=1; i <= daysToAdd; i++) {
            daysArray.add(29+i);
        }
    }

    public void startOver (View view) {

        resetApp();
        startOverButton.setVisibility(View.INVISIBLE);
        enterButton.setVisibility(View.VISIBLE);
        whiteLabel.setVisibility(View.VISIBLE);
        resultTextView.setText("");
    }

    public void resetApp () {

        resultTextView.setText("Select Month");
        enterButton.setText("Submit Month");
        yearEntered.setText("");
        yearEntered.setVisibility(View.INVISIBLE);
        label.setText("Month: ");
        label.setVisibility(View.VISIBLE);

        date = "";
        isMonthSelected = false;
        isDaySelected = false;
        isYearSelected = false;
        month = "";
        day = "";
        year = "";

        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);

        if (daysArray.size() > 29) {
            int tempNum = daysArray.size();
            for (int i = 1; i <= (tempNum - 29); i++) {
                daysArray.remove(tempNum - i);
            }
        }
    }

    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        spinner = (Spinner) findViewById(R.id.spinner);

        layoutR = (RelativeLayout) findViewById(R.id.relativeLayout);
        layoutR.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard(view);
                return false;
            }
        });

        yearEntered = (EditText) findViewById(R.id.editTextYear);
        yearEntered.setVisibility(View.INVISIBLE);

        label = (TextView) findViewById(R.id.label);
        whiteLabel = (ImageView) findViewById(R.id.imageView2);
        resultTextView = (TextView) findViewById(R.id.textView);
        resultTextView.setMovementMethod(new ScrollingMovementMethod());

        enterButton = (Button) findViewById(R.id.button1);
        startOverButton = (Button) findViewById(R.id.button2);
        startOverButton.setVisibility(View.INVISIBLE);

        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, monthsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        for (int i = 1; i <= 29; i++) {
            daysArray.add(i);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!isMonthSelected) {
            month = parent.getItemAtPosition(position).toString();
        } else if (!isDaySelected) {
            day = parent.getItemAtPosition(position).toString();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int calculateLeapDays (int earlyYear, int futureYear) {

        int leapDays = 0;
        int counter = 0;
        int leapYear = 0;
        int extraLeapDays = 0;

        if ((futureYear - earlyYear) < 4) {
            counter = earlyYear;
            while (counter <= futureYear) {
                if (counter % 4 == 0) {
                    leapDays += 1;
                    if ((counter % 100 == 0) && (counter % 400 != 0)) {
                        leapDays -= 1;
                    }
                }
                counter += 1;
            }
        } else {
            for (int i = 1; i <= 4; i++) {
                if ((earlyYear + i - 1) % 4 == 0) {
                    leapDays += 1;
                    if (((earlyYear + i - 1) % 100 == 0) && ((earlyYear + i - 1) % 400 != 0)) {
                        leapDays -= 1;
                    }
                    leapYear = earlyYear + i - 1;
                }
            }
            leapDays += (futureYear - leapYear) / 4;
            extraLeapDays += leapDays/100;
            leapDays -= leapDays/25;
            leapDays += extraLeapDays;
        }
        return leapDays;
    }


}
