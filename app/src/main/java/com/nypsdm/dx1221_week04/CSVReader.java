package com.nypsdm.dx1221_week04;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader
{
    public static List<int[]> ReadCSVFile(Context context, String filePath)
    {
        List<int[]> tileMap = new ArrayList<>();

        try (InputStream inputStream = context.getAssets().open(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream)))
        {
            String line;

            while ((line = br.readLine()) != null)
            {
                // Trim the line to remove leading/trailing whitespaces
                line = line.trim();

                if (line.isEmpty())
                {
                    // Skip empty lines
                    continue;
                }

                String[] values = line.split(",");
                int[] rowValues = new int[values.length];

                // Populate the row with values
                for (int col = 0; col < values.length; col++)
                {
                    rowValues[col] = Integer.parseInt(values[col]);
                }

                // Add the row to the tileMap
                tileMap.add(rowValues);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return tileMap;
    }
}