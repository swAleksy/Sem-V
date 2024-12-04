/*************************************************************

      Monitor dla problemu czytelnicy i pisarze
                 
                 
**************************************************************/



#include <stdio.h>
#include <time.h>
#include <math.h>
#include <unistd.h>
#include <stdlib.h>
#include <pthread.h>
#include <signal.h>
#include <fstream>
#include <iostream>



using namespace std;





/****************** monitor bez WNW **************/

/*************   monitor czytelnicy i pisarze ************/





#define ile 1000
#define maxc 5       // liczba czytelnikow
#define maxp 20       // liczba pisarzy

int sigp=0,sigc=0,sig=0;



   

pthread_mutex_t z;


void* pisarz (void* k) { // funkcja watku (watek)
         long nr=(long)k;
         int n=ile;
	 
         while (n--) {
	    cp.poczatek_pisania();

         sigp=sigp+1;  	 

         usleep(rand()%1000000);
	
         sigp=0;

         cp.koniec_pisania();
                        
         }   
         pthread_mutex_lock(&z);  
         sig=sig+1; 
         pthread_mutex_unlock(&z);    
                       
return 0;     
}



void* czytelnik (void* k) { // funkcja watku (watek)
         long nr=(long)k;
         int n=ile;
         
        
         while (n--) {
       //  usleep(rand()%10000);
	    cp.poczatek_czytania();
         
         sigc=1;

	     usleep(rand()%1000000);

         sigc=0;
	
        cp.koniec_czytania();  
         }          

        pthread_mutex_lock(&z);
        sig=sig+1;
        pthread_mutex_unlock(&z);

return 0;     
}




int main(int arg, char **argv) {

pthread_t pi[maxp],cz[maxc];



for (long i=0;i<maxc;i++)
pthread_create (&cz[i],0,czytelnik,(void*)i);

for (long i=0;i<maxp;i++)
pthread_create (&pi[i],0,pisarz,(void*)i);


  while (sig<(maxp+maxc)){
     
   if (sigp>1) {printf ("\tblad:pisarz-pisarz\n"); _exit(0);}
   if (sigp*sigc>0) {printf ("\tblad:pisarz-czytelnik\n");_exit(0);}
  }


for (int i=0;i<maxc;i++)
pthread_join (cz[i],0);

for (int i=0;i<maxp;i++)
pthread_join (pi[i],0);




return 0;

}




