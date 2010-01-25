#!/bin/sh

for x in who what; do
    for y in is are was were; do
        echo "question_template(\"$x $y the #1 of #0\", has/3)."
        echo "question_template(\"$x $y a #1 of #0\", has/3)."
        echo "question_template(\"$x $y an #1 of #0\", has/3)."
        echo "question_template(\"$x $y #0's #1\", has/3)."
    done
done

cat << EOF
statement_template("#2 is #0's #1", has/3).
EOF
