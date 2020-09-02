package com.google.sps.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList; // import the ArrayList class



public class TFIDFCalculator {
    /**Set of terms present across all documents**/
     private Set<String> hash_Set = new HashSet<String>(); 

    /** documents with string that will generate the terms for hash_set**/
     private ArrayList<ArrayList<String>> docs;
     private ArrayList<String> stopWords;
     private Set<String> setStopWords;

   /**
   * @param documents list of list of strings
   */
     public TFIDFCalculator(ArrayList<ArrayList<String>> documents) {
         docs= documents;
         stopWords = new ArrayList<String>(Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "e.g.", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves"));
         setStopWords = new HashSet<>(stopWords);
         for(ArrayList<String> doc: documents){
             for(String word: doc){
                 if(setStopWords.contains(word))
                    continue;
                 hash_Set.add(word);
             }
         }
    }
/**
     * @param doc  list of strings
     * @param term String represents a term
     * @return term frequency of term in document
     */
    public double tf(ArrayList<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / doc.size();
    }

 /**
     * @param docs list of list of strings represents the dataset
     * @param term String represents a term
     * @return the inverse term frequency of term in documents
     */
    public double idf(ArrayList<ArrayList<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(docs.size() / n);
    }

    /**
     * @param doc  a text document
     * @param docs all documents
     * @param term term
     * @return the TF-IDF of term
     */
     
    public double tfIdf(ArrayList<String> doc, String term) {
        return tf(doc, term) * idf(docs, term);

    }

    /**
    * @return nested list of list storing tfidf value of each term in a document
    */
    public ArrayList<ArrayList<Double>> tfidfDocumentsVector(){
        ArrayList<ArrayList<Double>> docs_vectors= new ArrayList<ArrayList<Double>>();
        for(ArrayList<String> doc: docs){
            ArrayList<Double> doc_vec= new ArrayList<Double>();
            for(String word:hash_Set){
                doc_vec.add(tfIdf(doc, word));
            }
            docs_vectors.add(doc_vec);

        }
        return docs_vectors;
    }

    public  ArrayList<ArrayList<String>> tfidfWords(){
        ArrayList<ArrayList<String>> docs_vectors= new ArrayList<ArrayList<String>>();
        for(ArrayList<String> doc: docs){
            ArrayList<String> doc_vec= new ArrayList<String>();
            for(String word:hash_Set){
                doc_vec.add(word);
            }
            docs_vectors.add(doc_vec);
        }
        return docs_vectors;
    }




    


}



