FILES=bin1data/*.BPP
for f in $FILES
do
    echo $f
    java -classpath out/production/evolutiva-ag/ otimizacao.Otimization output.txt $f
done