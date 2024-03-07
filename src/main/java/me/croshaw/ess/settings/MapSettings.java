package me.croshaw.ess.settings;

import me.croshaw.ess.exception.WrongCoordinatesException;
import me.croshaw.ess.util.Pair;
import me.croshaw.ess.util.Range;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class MapSettings extends DefaultSettings implements Serializable, Cloneable {
    public final static Range<Integer> ROWS_OR_COLUMNS_COUNT_RANGE = new Range<>(4, 15);
    private int rows;
    private int columns;
    private int[][] map;
    private HashMap<Integer, Pair<Integer, Integer>> companyPriorities;
    public void fillRandomly(Random random)  {
        this.rows = ROWS_OR_COLUMNS_COUNT_RANGE.getRandom(random);
        this.columns = ROWS_OR_COLUMNS_COUNT_RANGE.getRandom(random);
        map = new int[rows][columns];
        companyPriorities.clear();
        ArrayList<Integer> freePriorities = getFreePriorities();
        Collections.shuffle(freePriorities);
        for(int i = 0; i < freePriorities.size(); i++) {
            try {
                if (!addCompany(random.nextInt(0, rows), random.nextInt(0, columns), freePriorities.get(i)))
                    i--;
            }
            catch (WrongCoordinatesException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
    @Override
    public void fillDefault() {
        companyPriorities = new HashMap<>();
        rows = ROWS_OR_COLUMNS_COUNT_RANGE.getMin();
        columns = ROWS_OR_COLUMNS_COUNT_RANGE.getMin();
        map = new int[rows][columns];
    }

    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }
    private boolean isValidCords(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < columns;
    }
    public boolean hasCompany(int x, int y) throws WrongCoordinatesException {
        if(!isValidCords(x, y))
            throw new WrongCoordinatesException();
        return map[x][y] != 0;
    }
    public boolean addCompany(int x, int y, int priority) throws WrongCoordinatesException {
        if(hasCompany(x, y) || companyPriorities.containsKey(priority))
            return false;
        map[x][y] = priority;
        companyPriorities.put(priority, new Pair<>(x, y));
        return true;
    }
    public void removeCompany(int x, int y) throws WrongCoordinatesException {
        if(hasCompany(x, y)) {
            companyPriorities.remove(map[x][y]);
            map[x][y] = 0;
        }
    }
    public ArrayList<Integer> getFreePriorities() {
        ArrayList<Integer> result = new ArrayList<>();
        for(int start = 1; start <= CompanySettings.COMPANY_COUNT_RANGE.getMax(); start++) {
            if(!companyPriorities.containsKey(start))
                result.add(start);
        }
        return result;
    }
    public void resize(int newRows, int newColumns) {
        if(newRows < ROWS_OR_COLUMNS_COUNT_RANGE.getMin() || newColumns < ROWS_OR_COLUMNS_COUNT_RANGE.getMin())
            throw new IllegalArgumentException();
        if(newRows == rows && newColumns == columns)
            return;
        int[][] newMap = new int[newRows][newColumns];
        for(int i = 0; i < Math.min(newRows, rows); i++) {
            if (Math.min(newColumns, columns) >= 0)
                System.arraycopy(map[i], 0, newMap[i], 0, Math.min(newColumns, columns));
        }
        rows = newRows;
        columns = newColumns;
        map = newMap;
    }
    public int getCompanyCount() {
        return companyPriorities.size();
    }
    public int[][] getMap() {
        return map;
    }

    @Override
    public MapSettings clone() {
        try {
            return (MapSettings) super.clone();
        } catch (CloneNotSupportedException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
