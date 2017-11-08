#!/bin/bash
ant -Dprofile=test clean ear
ant -Dprofile=quality clean ear
ant -Dprofile=prod clean ear