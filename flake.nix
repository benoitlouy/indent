{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixpkgs-unstable";
    typelevel-nix = {
      url = "github:typelevel/typelevel-nix";
      inputs.nixpkgs.follows = "nixpkgs";
    };
    flake-utils.follows = "typelevel-nix/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils, typelevel-nix }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          overlays = [ typelevel-nix.overlay ];
        };
        jdk = pkgs.jdk11;
      in
      {
        devShell = pkgs.devshell.mkShell {
          imports = [ typelevel-nix.typelevelShell ];
          name = "scala shell for indent project";
          typelevelShell = {
            jdk.package = jdk;
            sbtMicrosites = {
              enable = false;
            };
          };
        };
      }
    );
}
