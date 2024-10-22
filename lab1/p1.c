/*************  tworzenie watkow ******************************
Funkcja tworzenia watku:
  pthread_create(&id_watku, &atrybut, funkcja_watku,(void*)par);
  id_watku   	- obiekt watku
  atrybut 	- obiekt atrybutu watku ( do ustawienia atrybutow tworzonego watku),
		  (0 - atrybut domyślny)
  funkcja_watku - funkcja ktora wykonuje sie jako nowy watek
  par   	- parametr funkcji watku

Funkcja watku:
 void *fw( void *) - prototyp funkcji watku
 Funkcje watku dzialaja wspolbieznie (rownolegle) po utworzeniu
 watku (wywolanie  pthread_create ) w ramach tego samego procesu unixowego.
 Zmienne globalne programu sa wspolne dla wszystkich watkow

**************************************************************/

/******************************** ćwiczenie ******************************

1) uruchomić program kilka razy
2) odkomentować instrukcje: pthread_create i pthread_join,
   a wykomentowac p(0) i q(0). Skompilować i uruchomić program kilka razy

pytanie: w którym przypadku program jest współbieżny i dlaczego?

**************************************************************/


#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>

#define ile 100


void* p (void* l) { // funkcja watku (watek)
      int i=0;
      for (i=0;i<ile;i++) {
      printf("%d",1);
      fflush(stdout);
      }

     return 0;
}


void* q (void* l) { // funkcja watku (watek)

      int i=0;
      for (i=0;i<ile;i++) {
      printf("%d",2);
      fflush(stdout);
      }
      return 0;
}


int main () {
	pthread_t w1=0,w2=0;


    pthread_create(&w1, 0, p,0); // tworzy watek dla funkcji p
    pthread_create(&w2, 0, q,0); // tworzy watek dla funkcji q

//    p(0);   // wywolanie funkcji p w watku glownym
//    q(0);  //  wywolanie funkcji q w watku glownym


/**************************************************************
uwaga: Wątek główny tj. funkcja main wykonuje się niezależnie
od pozostałych wątków. Wątek główny jest uprzywilejowany ponieważ
jego zakończenie powoduje zakończenie wszystkich innych wątków.
Dlatego wątek główny nie powinien wywołać return lub exit dopóki
nie zakończą się pozostałe wątki.
***************************************************************/




    pthread_join(w1,NULL); 	   // czeka  az zakonczy sie watek p
    pthread_join(w2,NULL);      // czeka az zakonczy sie watek q


  printf("\nkoniec\n");



return 0;
}
