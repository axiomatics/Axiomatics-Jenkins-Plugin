# Axiomatics-Plugin-Jenkins

This software is the beginning of an effort to integrate policy development - whether in
ALFA or regular XACML - with Jenkins. 

Please note that this software is currently in Alpha and your feedback about improving this software is very much appreciated.

IMPORTANT: This plugin is **not** an official Axiomatics release and is my own personal development.

## Current Features

When you add the Axiomatics Policy Deployment Build Step to a project, these features can now be added there in GUI:

- ASM URL
- WSDL URL
- ASM User
- ASM Password (thoughts on improving security are welcome)
- Truststore
- Truststore type
- Truststore password (same thing on security)
- Domain name
- Project name

The policy to be packaged and deployed is still hardcoded policy in Java.

## Upcoming Features

- Parameterized input for your build through Jenkins
- Git integration

## Notes

Currently, the Git plugin is part of this plugin but testing needs to be done about how it
works with deployment. 

## Want to help?

Contributors are very welcome! I look forward to your pull request. Questions? Please contact me at mgood@axiomatics.com. 

## Special Thanks
Special thanks to David Brossard, Andrés Martinelli, and Jonas Iggbom, as some of the code is or is directly derived from their work. 
