package service;

import exception.EntityNotFoundException;
import model.State;

import java.util.List;

public interface StateService {
    State create(State state);
    State readById(long id) throws EntityNotFoundException;
    State update(State state) throws EntityNotFoundException;
    void delete(long id) throws EntityNotFoundException;

    State getByName(String name) throws EntityNotFoundException;
    List<State> getAll();
}
