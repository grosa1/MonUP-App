package giovanni.tradingtoolkit.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Created by giiio on 17/12/2017.
 */

public class ResponseData {
    @SerializedName("id")
    @Expose
    private String id;


    public ResponseData(String id) {

        this.id = id;
    }

    public String getId() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseData coin = (ResponseData) o;

        return Objects.equals(id, coin.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
