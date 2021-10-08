package ua.com.foxminded.university.data.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Tabletime {

    private LocalDateTime begin;
    private LocalDateTime end;
    private List<TabletimeRow> rows;

    public Tabletime(LocalDateTime begin, LocalDateTime end,
            List<TabletimeRow> rows) {
        super();
        this.begin = begin;
        this.end = end;
        this.rows = rows;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public List<TabletimeRow> getRows() {
        return rows;
    }

    public void setBegin(LocalDateTime begin) {
        this.begin = begin;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setRows(List<TabletimeRow> rows) {
        this.rows = rows;
    }

    @Override
    public int hashCode() {
        return Objects.hash(begin, end, rows);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tabletime other = (Tabletime) obj;
        return Objects.equals(begin, other.begin)
                && Objects.equals(end, other.end)
                && Objects.equals(rows, other.rows);
    }

}
