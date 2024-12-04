#include <pthread.h>
#include <iostream>

class ReadersWriters {
private:
    int readersCount;
    pthread_mutex_t mutex;
    pthread_cond_t writersCondition;
    bool writerActive;

public:
    ReadersWriters() : readersCount(0), writerActive(false) {
        pthread_mutex_init(&mutex, nullptr);
        pthread_cond_init(&writersCondition, nullptr);
    }

    ~ReadersWriters() {
        pthread_mutex_destroy(&mutex);
        pthread_cond_destroy(&writersCondition);
    }

    void startRead() {
        pthread_mutex_lock(&mutex);
        while (writerActive) {
            pthread_cond_wait(&writersCondition, &mutex);
        }
        readersCount++;
        pthread_mutex_unlock(&mutex);
    }

    void endRead() {
        pthread_mutex_lock(&mutex);
        readersCount--;
        if (readersCount == 0) {
            pthread_cond_signal(&writersCondition);
        }
        pthread_mutex_unlock(&mutex);
    }

    void startWrite() {
        pthread_mutex_lock(&mutex);
        while (writerActive || readersCount > 0) {
            pthread_cond_wait(&writersCondition, &mutex);
        }
        writerActive = true;
        pthread_mutex_unlock(&mutex);
    }

    void endWrite() {
        pthread_mutex_lock(&mutex);
        writerActive = false;
        pthread_cond_broadcast(&writersCondition);
        pthread_mutex_unlock(&mutex);
    }
};
