#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef int (*CompFunc)(const void *, const void *);

int AscCompFunc(const void *a, const void *b) {
	int l = *((int *)a);
	int r = *((int *)b);
	
	return((l > r) - (l < r));
}

int DescCompFunc(const void *a, const void *b) {
	int l = *((int *)a);
	int r = *((int *)b);
	
	return((l < r) - (l > r));
}

int BinarySearch(int *arr, int length, int key, CompFunc compfunc) {
	int low = 0, high = (length - 1);
	
	while (low <= high) {
		int mid = low + ((high - low)/2);
		
		/* Check Mid Element */
		if(compfunc(&key, (arr+mid)) == 0)
			return mid;
		/* Search Left Sub-array */
		else if(compfunc(&key, (arr+mid)) < 0) 
			high = mid -1;
		/* Search Right sub-array */
		else 
			low = mid + 1;
	}
	
	return -1;
}

int main() {
	int ascarr[] = {1,2,3,4,5,6,7,8,9};
	int descarr[] = {11,10,9,8,7,6,5,4,3,2,1};
	int searchidx;
	
	searchidx = BinarySearch(ascarr, 8, 5, AscCompFunc);
	
	printf("Key found at %d\n", searchidx);

	searchidx = BinarySearch(descarr, 8, 11, DescCompFunc);
	
	printf("Key found at %d\n", searchidx);	
}