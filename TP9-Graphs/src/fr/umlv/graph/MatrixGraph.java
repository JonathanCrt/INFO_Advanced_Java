package fr.umlv.graph;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

// Personne ne pourra hériter des méthodes de la classes
final class MatrixGraph<T> implements Graph<T> {
	
	private final T[] mat;
	private final int n;
	
	
	@SuppressWarnings("unchecked")
	MatrixGraph(int n) { //visibilité package
		if(n <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.mat = (T[]) new Object[n * n];
		this.n = n;
	}
	
	private int index(int i, int j) {
		return i * n + j ;
	}
	
	private void checkEdge(int i, int j) {
		Objects.checkIndex(i, n);
		Objects.checkIndex(j, n);
	}
	

	@Override
	public Optional<T> getWeight(int src, int dst) {
		this.checkEdge(src, dst);
		var i =  index(src, dst);
		return (this.mat[i] == null) ? Optional.empty() : Optional.of(mat[i]);
	}

	@Override
	public void addEdge(int src, int dst, T weight) {
		this.checkEdge(src, dst);
		Objects.requireNonNull(weight);
		var elt = this.index(src, dst);
		this.mat[elt] = weight;
	}


	@Override
	public Iterator<Integer> neighborIterator(int src) {
		
		return new Iterator<Integer>() {
			
			private int index = this.nextToReturn(0); // index du prochain a renvoyer
			private int tmp;
			private boolean nextToRemove = false;
			
			
			private int nextToReturn(int from) { // prochain à renvoyer en commençant à from.
				for(var dst = from; dst < n; dst++) {
					if(mat[index(src, dst)] != null) {
						return dst;
					}
				}
				return -1; // -1 si plus personne
			}
			
			@Override
			public boolean hasNext() {
				return index != -1;				
			}

			@Override
			public Integer next() { // Pas le droit de l'utiliser si on a pas vérifier hasNext()
				if(!this.hasNext()) {
					throw new NoSuchElementException(); // renvoie noSuchElementException si hasNext retourne false
				}
				tmp = index; 
				this.nextToRemove = true;
				index = this.nextToReturn(index + 1);
				return tmp;            
			}
			
			
			@Override
		    public void remove() {
		        if (nextToRemove) {
		        	nextToRemove = false;
		        }
		        else {
		        	throw new IllegalStateException();
		        }
		        mat[src * n + tmp] = null;
		    }
		};
	}
	

	/*
	 * Q1 - On multiplie i par la taille du tableau et un ajoute j
	 * Q2 - Erasure -> on ne peut pas créer un tableau de type paramétré (car le type param. n'existe plus à l'éxécution), sans le cast on doit caster les elements
	 * a chaque fois qu'on les insére. Je garanti qu'il n'u aura que des T dedans. Pas de getter sur la tableau
	 * Q4 - Optional -> Dans le cas ou le résultat d'un calcul ne peut pas exister, au lieu d'utiliser null, on utilise Optional
	 * nb :  static -> type param pas lié avec la type de la classe
	 * Q7 - remove(..) est déja implémenter dans l'interface Iterator comme méthode par défaut
	 */
	
	
	
	
}
