package citra;

import citra.client.*;

public record Client(User user, Token token, Comm comm, Address address) {
}
