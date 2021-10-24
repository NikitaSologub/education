package by.itacademy.sologub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntity {
    private int id;

    public AbstractEntity withId(int id){
        setId(id);
        return this;
    }
}