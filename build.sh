#!/bin/bash
ant -Dprofile=test clean build
ant -Dprofile=quality clean build
ant -Dprofile=prod clean build