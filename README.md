The src/kmeans folder contains the folder with the following classes:

Term.java - Term with its value and its frequency in document

Document.java - Document class with name, cluster index, tf-idf and score
ModDocUtils.java -  Tokenize document, standardise the tokens and map conversion
        function for each line. This version is a modified from the
        original DocUtils class by Scott Sanner

HashMapBuilder.java - Creates a dictionary of file as keys to a dictionary of
        term with its frequencies

VectorRepresentation.java - Create a vector representation of the documents and
        mapping the tf-idf for each document

Cluster.java - Create clusters from generated centroids and a list of
        documents containing terms with its tf-idf

Helper.java - Helper methods to calculate similarity score and print x number
        of documents after running K-means algorithm

Kmeans.java - Randomly initialise centroids of the size of document list,
        maximise the calculated cosine similarity from the helper
        class and run k-means for a maximum number of provided iterations
        to obtain the required number of clusters with names for the documents
