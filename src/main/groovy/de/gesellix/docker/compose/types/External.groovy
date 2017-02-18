package de.gesellix.docker.compose.types

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString
class External {

    boolean external
    String name

}