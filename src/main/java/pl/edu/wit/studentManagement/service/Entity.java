package pl.edu.wit.studentManagement.service;

import pl.edu.wit.studentManagement.exceptions.ValidationException;

import java.io.Serializable;
import java.util.UUID;

abstract class Entity implements Serializable {
    abstract void validate() throws ValidationException;
    abstract UUID getId();
}
