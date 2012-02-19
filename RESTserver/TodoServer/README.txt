Hello group!

Here's a quick 2s explanation of what needs to be done to get the server up and running, if you're interested tonight.

=========
[1rst, assurer vous de configurer ca comme un project maven avec m2e sous eclipse, et rouler update config dans l'onglet maven sur un right-click]

ca prend un peu de setup.
en gros, y'a 2 target eclipse a configurer.
un qui build la BD, basé sur les infos dans le hibernate.todo.cfg.xml
(faut donc creer un schema vide sur une BD mysql qui roule sur le poste sur lequel tu pars ca.)
ensuite y'a simplement a partir le Launcher.
suffit de ce brancher sur http://localhost:8080/application.wadl pour voir si le serveur est up.