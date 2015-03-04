#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

typedef int BOOL;
enum {
	FALSE = 0,
	TRUE
};

typedef struct {
	int *queue;
	int qcapacity;
	int qlength;
	int first;
	int last;
} Queue;

void queueInit (Queue *q) {
	memset(q, 0, sizeof(Queue));
	q->queue = malloc(2*sizeof(int));
	q->qcapacity = 2;
}

BOOL isEmpty (Queue *q) {
	return (q->qlength == 0);
}

int qSize (Queue *q) {
	return (q->qlength);
}

void resizeQueue(Queue *q, int capacity) {
	int *temp, i;
	
	assert(capacity >= q->qlength);
	temp = malloc(capacity * sizeof(int));
	for(i = 0; i < q->qlength; i++) {
		int qidx = (q->first + i) % q->qcapacity;
		temp[i] = q->queue[qidx];
	}
	free(q->queue); // free memory for the old queue
	q->queue = temp; // make the queue array point to the resized array
	q->qcapacity = capacity;
	q->first = 0;
	q->last = q->qlength;
}

void enqueue (Queue *q, int item) {
	/* Double the array when full */
	if(q->qlength == q->qcapacity) {
		resizeQueue(q, 2*q->qcapacity);
	}
	q->queue[q->last++] = item;
	if(q->last == q->qcapacity) 
		q->last = 0;
	
	q->qlength++;
}

int dequeue (Queue *q) {
	int item = q->queue[q->first++];
	if(q->first == q->qcapacity)
		q->first = 0;
	q->qlength--;
	// Shrink if queue occupancy is 1/4th capacity
	if(q->qlength > 0 && q->qlength == q->qcapacity/4) {
		resizeQueue(q, q->qcapacity/4);
	}
	return(item);
}

void main() {
	int numitems = 0, item = 0, i;
	Queue q;
	
	queueInit(&q);
	
	printf("Enter number of items\n");
	scanf("%d",&numitems);
	
	for(i = 0; i < numitems; i++) {
		scanf("%d",&item);
		enqueue(&q, item);
		
		if(i % 2) {
			printf("Dequeue\n");
			item = dequeue(&q);
			printf("%d\n",item);
		}
	}
	
	printf("Dequeue\n");
	while(!isEmpty(&q)) {
		item = dequeue(&q);
		printf("%d\n",item);
	}
}