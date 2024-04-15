{ pkgs ? import <nixpkgs> {} }:

with pkgs;
mkShell {
    shellHook = ''
        export LD_LIBRARY_PATH="$LD_LIBRARY_PATH:${with xorg; lib.makeLibraryPath [ libGL ]}"
    '';
}
