#!/usr/local/bin/python3
"""This script sends a route from a GPX file to the Android emulator.

usage: routing [-h] [-s HOST] [-p PORT] [-x SPEED] [-t STEP] gpx

positional arguments:
  gpx                   A GPX file containing routing information

optional arguments:
  -h, --help            show this help message and exit
  -s HOST, --host HOST  Host
  -p PORT, --port PORT  Port
  -x SPEED, --speed SPEED
                        Speed in km/h
  -t STEP, --step STEP  Step distance in meters


Copyright (C) 2015 Simon Kalt
"""
import telnetlib
import argparse
import gpxpy
from gpxpy.gpx import GPXTrackPoint
import time


def read_gpx_file(file):
    """Read GPX data from the given file."""
    with open(file, 'r') as handle:
        gpx = gpxpy.parse(handle)
    return gpx


def send_point(connection, point):
    """Send the given point to the Android emulator via telnet."""
    location_to_send = ('geo fix {longitude} {latitude}\n'
                        .format(**point.__dict__))
    print('Sending:', location_to_send, end='')
    bytestr = location_to_send.encode('ascii')
    connection.write(bytestr)


def equalize_distance(points, step_distance=10):
    """Equalize the distances between the given points.

    Given a list of GPX points, this function inserts points in the route
    through linear interpolation so that no two points have a distance of
    more than `step_distance` meters.
    """
    length = len(points)

    result_points = []

    for i in range(length - 1):
        p1 = points[i]
        p2 = points[i+1]
        dist = p1.distance_2d(p2)

        result_points.append(p1)

        if dist > step_distance:
            n = int(dist // step_distance)

            dist_x = p2.longitude - p1.longitude
            dist_y = p2.latitude - p1.latitude

            diff_x = dist_x / (n + 1)
            diff_y = dist_y / (n + 1)

            for j in range(1, n + 1):
                new_lon = p1.longitude + j * diff_x
                new_lat = p1.latitude + j * diff_y

                new_point = GPXTrackPoint(new_lat, new_lon)
                result_points.append(new_point)

        result_points.append(p2)

    return result_points


def run(file, host='127.0.0.1', port=5554, speed=20, step_distance=5):
    """Send the route given in the GPX file to the Android emulator."""
    ms = kmh_to_ms(speed)
    sleep = step_distance / ms

    conn = telnetlib.Telnet(host, port)

    gpx = read_gpx_file(file)

    for track in gpx.tracks:
        for seg in track.segments:
            points = equalize_distance(seg.points, step_distance)

            if len(seg.points) > 0:
                first = seg.points[0]
                send_point(conn, first)

            input('Press enter to start sending the route...')

            for point in points:
                send_point(conn, point)
                time.sleep(sleep)

    conn.close()


def kmh_to_ms(kmh):
    """Convert kilometers/hour to meters/second."""
    return kmh / 3.6


if __name__ == '__main__':
    parser = argparse.ArgumentParser(prog='routing')
    parser.add_argument('gpx',
                        help='A GPX file containing routing information',
                        type=str)
    parser.add_argument('-s', '--host', type=str,
                        default='127.0.0.1', help='Host')
    parser.add_argument('-p', '--port', type=int,
                        default=5554, help='Port')
    parser.add_argument('-x', '--speed', type=int,
                        default=20, help='Speed in km/h')
    parser.add_argument('-t', '--step', type=int,
                        default=10, help='Step distance in meters')

    args = parser.parse_args()

    run(args.gpx, args.host, args.port, args.speed, args.step)
