package org.jsp.Banking.Helper;

import lombok.Data;

@Data
public class ResponseStructure<T> {
int code;
String message;
T data;

}
