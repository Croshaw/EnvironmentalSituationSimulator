package me.croshaw.ess.settings;

import me.croshaw.ess.exception.WrongCoordinatesException;

import java.io.Serializable;
import java.util.Random;

public class MapSettings extends DefaultSettings implements Serializable, Cloneable {
    private int rows;
    private int columns;
    private boolean[][] map;
    public void fillRandomly(Random random)  {
        this.rows = random.nextInt(2, 15);
        this.columns = random.nextInt(3, 15);
        map = new boolean[rows][columns];
        int companyCount = random.nextInt(5, Math.min(12, columns*rows)+1);
        for(int i = 0; i < companyCount; i++) {
            try {
                if (!addCompany(random.nextInt(0, rows), random.nextInt(0, columns)))
                    i--;
            }
            catch (WrongCoordinatesException exception) {
                //!
            }
        }
    }
    @Override
    public void fillDefault() {
        rows = 2;
        columns = 3;
        map = new boolean[rows][columns];
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
        return map[x][y];
    }
    public boolean addCompany(int x, int y) throws WrongCoordinatesException {
        if(hasCompany(x, y))
            return false;
        map[x][y] = true;
        return true;
    }
    public void removeCompany(int x, int y) throws WrongCoordinatesException {
        if(hasCompany(x, y))
            map[x][y] = false;
    }
    public void resize(int newRows, int newColumns) {
        if(newRows < 2 || newColumns < 3)
            throw new IllegalArgumentException();
        if(newRows == rows && newColumns == columns)
            return;
        boolean[][] newMap = new boolean[newRows][newColumns];
        for(int i = 0; i < Math.min(newRows, rows); i++) {
            if (Math.min(newColumns, columns) >= 0)
                System.arraycopy(map[i], 0, newMap[i], 0, Math.min(newColumns, columns));
        }
        rows = newRows;
        columns = newColumns;
        map = newMap;
    }
    public int getCompanyCount() {
        int result = 0;
        for(int i = 0; i < SimulationSettings.MAP.getRows(); i++) {
            for(int j = 0; j < SimulationSettings.MAP.getColumns(); j++) {
                if(SimulationSettings.MAP.getMap()[i][j])
                    result++;
            }
        }
        return result;
    }
    public boolean[][] getMap() {
        return map;
    }

    @Override
    public MapSettings clone() {
        try {
            MapSettings clone = (MapSettings) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
