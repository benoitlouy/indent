{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixpkgs-unstable";
    devshell.url = "github:numtide/devshell";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils, devshell }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          overlays = [ devshell.overlays.default ];
        };
        jdk = pkgs.jdk11;
        sbt = pkgs.sbt.override { jre = jdk; };
      in
      {
        devShell = pkgs.devshell.mkShell {
          name = "scala shell for indent project";
          packages = [
            jdk
            sbt
            pkgs.scala-cli
          ];
        };
      }
    );
}
