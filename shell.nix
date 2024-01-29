{ pkgs ? import <nixpkgs> {} }:

with pkgs;
mkShell {
  LD_LIBRARY_PATH="${with xorg; lib.makeLibraryPath [ libGL ]}";
}
